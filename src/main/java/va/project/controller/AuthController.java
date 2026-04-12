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

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> regiser(@Valid @RequestBody RegisterRequest requestDto){
        RegisterResponse responseDto = userService.register(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
