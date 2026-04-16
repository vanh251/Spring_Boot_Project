package va.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import va.project.dto.request.RegisterRequest;
import va.project.dto.response.RegisterResponse;
import va.project.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import va.project.dto.request.LoginRequest;
import va.project.dto.response.LoginResponse;
import va.project.security.jwt.JwtProvider;
import va.project.security.principle.UserDetailCustom;
import va.project.service.RefreshTokenService;
import va.project.entity.RefreshToken;
import va.project.dto.request.TokenRefreshRequest;
import va.project.dto.response.TokenRefreshResponse;
import va.project.exception.TokenRefreshException;
import va.project.security.principle.UserDetailsServiceCustom;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceCustom userDetailsServiceCustom;

    @PostMapping("/register")
    public ResponseEntity<?> regiser(@Valid @RequestBody RegisterRequest requestDto){
        RegisterResponse responseDto = userService.register(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailCustom userDetails = (UserDetailCustom) authentication.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        String role = "";
        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            role = userDetails.getAuthorities().iterator().next().getAuthority();
        }

        LoginResponse loginResponse = LoginResponse.builder()
                .token(accessToken)
                .type("Bearer")
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .role(role)
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetailCustom userDetails = (UserDetailCustom) userDetailsServiceCustom.loadUserByUsername(user.getUsername());
                    String token = jwtProvider.generateAccessToken(userDetails);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken, "Bearer"));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token không tồn tại trong DB!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody TokenRefreshRequest request) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
        return ResponseEntity.ok(Map.of("message", "Đã đăng xuất thành công!"));
    }
}
