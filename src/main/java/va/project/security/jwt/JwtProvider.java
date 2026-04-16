package va.project.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import va.project.security.principle.UserDetailCustom;

@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expired;

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // sinh Token
    public String generateAccessToken(UserDetailCustom userDetails){
        Date now = new Date();
        String role = "";
        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            role = userDetails.getAuthorities().iterator().next().getAuthority();
            if (role.startsWith("ROLE_")) {
                role = role.substring(5);
            }
        }
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+expired)) //thoi han 24h
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetailCustom userDetails){ // thời hạn lâu hơn 7 ngày
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+expired*7))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    //xác minh token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Token bị sai định dạng: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Hệ thống không hỗ trợ loại token này: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Chuỗi token bị trống: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Thẻ đã hết hạn sử dụng: {}", e.getMessage());
        }
        return false;
    }

    public String getUserNameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
