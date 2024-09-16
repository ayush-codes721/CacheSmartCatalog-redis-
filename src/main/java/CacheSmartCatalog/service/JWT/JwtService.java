package CacheSmartCatalog.service.JWT;

import CacheSmartCatalog.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    // Define expiration for access token (30 minutes)
    private static final long ACCESS_TOKEN_EXPIRATION = TimeUnit.MINUTES.toMillis(30);
    // Define expiration for refresh token (6 months)
    private static final long REFRESH_TOKEN_EXPIRATION = TimeUnit.DAYS.toMillis(180);

    // Create SecretKey instance
    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Create access token method
    public String createAccessToken(User user) {
        Date expirationDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(secretKey())
                .compact();
    }

    // Create refresh token method
    public String createRefreshToken(User user) {
        Date expirationDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION);

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(secretKey())
                .compact();
    }

    // Get claims from token
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get ID from token
    public Long getIdFromToken(String token) {
        Claims claims = getClaims(token);
        String subject = claims.getSubject();
        return Long.valueOf(subject);
    }

    // Get username from token
    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }
}
