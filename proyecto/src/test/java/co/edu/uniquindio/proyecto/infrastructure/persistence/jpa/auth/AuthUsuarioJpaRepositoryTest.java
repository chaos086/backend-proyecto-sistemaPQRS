package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica consulta por VO embebido y JPQL.
 */
@SpringBootTest
@Transactional
class AuthUsuarioJpaRepositoryTest {

    @Autowired
    AuthUsuarioJpaRepository repository;

    @Test
    void buscarPorCorreoJpql_resuelveValorEmbebido() {
        repository.save(new AuthUsuarioEntity(
                UUID.randomUUID().toString(),
                "jpqlvo@uniquindio.edu.co",
                "{noop}pass",
                "ESTUDIANTE"));

        assertThat(repository.buscarPorCorreoJpql("JPQLVO@uniquindio.edu.co")).isPresent();
    }
}