package co.edu.uniquindio.proyecto.config;

import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth.AuthUsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth.AuthUsuarioJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

/**
 * Usuario demo consultable en Postman contra la BD embebida H2 (contraseña hasheada BCrypt).
 */
@Configuration
public class AuthUsuarioDataBootstrap {

    public static final String DEMO_EMAIL = "demo@uniquindio.edu.co";
    public static final String DEMO_PASSWORD = "Demo#12345";

    @Bean
    CommandLineRunner crearUsuarioDemoSiVacio(AuthUsuarioJpaRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.save(new AuthUsuarioEntity(
                    UUID.randomUUID().toString(),
                    DEMO_EMAIL,
                    passwordEncoder.encode(DEMO_PASSWORD),
                    "COORDINADOR"));
        };
    }
}
