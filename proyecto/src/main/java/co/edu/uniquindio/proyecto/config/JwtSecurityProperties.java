package co.edu.uniquindio.proyecto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Propiedades del token JWT (secreto HMAC y tiempo de vida).
 */
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtSecurityProperties {

    /**
     * Secreto HMAC-SHA256 (mínimo 256 bits recomendado para jjwt Keys.hmacShaKeyFor).
     */
    private String secret = "desarrollo-cambiar-en-produccion-minimo-32-caracteres-seguros-hs256";

    /**
     * Duración del Bearer token (vida media con expiración explícita en el JWT).
     */
    private Duration expiration = Duration.ofHours(4);

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getExpiration() {
        return expiration;
    }

    public void setExpiration(Duration expiration) {
        this.expiration = expiration;
    }
}
