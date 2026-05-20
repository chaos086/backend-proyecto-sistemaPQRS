package co.edu.uniquindio.proyecto.config;

import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth.AuthUsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth.AuthUsuarioJpaRepository;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs.UsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs.UsuarioEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class AuthUsuarioDataBootstrap {

    public static final String DEMO_EMAIL = "demo@uniquindio.edu.co";
    public static final String DEMO_PASSWORD = "Demo#12345";
    public static final String STUDENT_EMAIL = "estudiante@uniquindio.edu.co";
    public static final String STUDENT_PASSWORD = "Estudiante#12345";

    @Bean
    CommandLineRunner crearUsuarioDemoSiVacio(
            AuthUsuarioJpaRepository authRepo,
            UsuarioEntityRepository domainRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (authRepo.count() > 0) {
                return;
            }
            String demoId = UUID.randomUUID().toString();
            String studentId = UUID.randomUUID().toString();

            authRepo.save(new AuthUsuarioEntity(demoId, DEMO_EMAIL,
                    passwordEncoder.encode(DEMO_PASSWORD), "COORDINADOR"));
            authRepo.save(new AuthUsuarioEntity(studentId, STUDENT_EMAIL,
                    passwordEncoder.encode(STUDENT_PASSWORD), "ESTUDIANTE"));

            domainRepo.save(new UsuarioEntity(demoId, "Demo Coordinador",
                    Rol.COORDINADOR, EstadoUsuario.ACTIVO, DEMO_EMAIL));
            domainRepo.save(new UsuarioEntity(studentId, "Estudiante Demo",
                    Rol.ESTUDIANTE, EstadoUsuario.ACTIVO, STUDENT_EMAIL));
        };
    }
}
