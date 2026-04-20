package co.edu.uniquindio.proyecto.security;

import co.edu.uniquindio.proyecto.config.JwtSecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Emisión y validación de JWT firmados (HS256), con reclamación de expiración estándar.
 */
@Service
public class JwtService {

    private final JwtSecurityProperties props;
    private final SecretKey signingKey;

    public JwtService(JwtSecurityProperties props) {
        this.props = props;
        byte[] keyBytes = props.getSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("app.security.jwt.secret debe tener al menos 32 bytes para HS256");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String subjectUserId, String email, String rolDominio) {
        Instant now = Instant.now();
        Instant exp = now.plus(props.getExpiration());
        String authority = "ROLE_" + rolDominio;
        return Jwts.builder()
                .subject(email)
                .claim("uid", subjectUserId)
                .claim("roles", List.of(authority))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseAndValidate(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpiresInSeconds() {
        return props.getExpiration().getSeconds();
    }
}
