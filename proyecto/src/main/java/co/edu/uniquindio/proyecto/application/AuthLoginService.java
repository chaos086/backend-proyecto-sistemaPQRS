package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.infrastructure.api.dto.LoginRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.TokenResponse;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth.AuthUsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth.AuthUsuarioJpaRepository;
import co.edu.uniquindio.proyecto.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso de aplicación: login contra credenciales persistidas en BD y emisión de JWT.
 */
@Service
public class AuthLoginService {

    private final AuthUsuarioJpaRepository authUsuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthLoginService(
            AuthUsuarioJpaRepository authUsuarioJpaRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.authUsuarioJpaRepository = authUsuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        AuthUsuarioEntity row = authUsuarioJpaRepository
                .buscarPorCorreoJpql(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), row.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateAccessToken(row.getId(), row.getEmail(), row.getRol());
        return new TokenResponse(token, "Bearer", jwtService.getExpiresInSeconds());
    }
}
