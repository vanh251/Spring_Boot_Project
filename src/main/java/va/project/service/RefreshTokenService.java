package va.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import va.project.entity.RefreshToken;
import va.project.repository.RefreshTokenRepository;
import va.project.repository.UserRepository;
import va.project.exception.TokenRefreshException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    // Để 7 ngày: 7 * 24 * 60 * 60 * 1000 = 604800000L
    private Long refreshTokenDurationMs = 604800000L;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        
        // Thu hồi (xóa) token cũ của User nếu có (Yêu cầu của người dùng)
        refreshTokenRepository.deleteByUser(user);

        // Tạo token mới
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token đã hết hạn. Vui lòng đăng nhập lại.");
        }
        return token;
    }

    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}
