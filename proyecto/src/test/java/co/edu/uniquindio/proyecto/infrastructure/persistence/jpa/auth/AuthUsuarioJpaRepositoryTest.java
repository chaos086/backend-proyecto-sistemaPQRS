package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica consulta por VO embebido, JPQL y paginación nativa con límite SQL.
 */
@DataJpaTest
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

    @Test
    void buscarPorRolPaginadoNativo_aplicaPageable() {
        repository.save(new AuthUsuarioEntity(UUID.randomUUID().toString(), "a1@uq.edu.co", "x", "COORDINADOR"));
        repository.save(new AuthUsuarioEntity(UUID.randomUUID().toString(), "a2@uq.edu.co", "x", "COORDINADOR"));
        repository.save(new AuthUsuarioEntity(UUID.randomUUID().toString(), "b1@uq.edu.co", "x", "ESTUDIANTE"));

        Page<AuthUsuarioEntity> page = repository.buscarPorRolPaginadoNativo(
                "COORDINADOR",
                PageRequest.of(0, 1));

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(1);
    }
}
