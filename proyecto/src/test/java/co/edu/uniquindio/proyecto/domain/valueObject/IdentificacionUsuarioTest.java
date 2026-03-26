package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IdentificacionUsuarioTest {

    @Test
    void crearIdentificacionValida() {
        IdentificacionUsuario id = IdentificacionUsuario.of("1098765432");
        assertNotNull(id);
        assertEquals("1098765432", id.valor());
    }

    @Test
    void crearIdentificacionNulaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> IdentificacionUsuario.of((String) null));
    }

    @Test
    void crearIdentificacionVaciaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> IdentificacionUsuario.of(""));
    }

    @Test
    void crearIdentificacionEnBlancoDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> IdentificacionUsuario.of("   "));
    }

    @Test
    void crearIdentificacionNewId() {
        IdentificacionUsuario id = IdentificacionUsuario.newId();
        assertNotNull(id);
        assertNotNull(id.valor());
        assertTrue(id.valor().length() > 0);
    }

    @Test
    void crearIdentificacionFromUUID() {
        UUID uuid = UUID.randomUUID();
        IdentificacionUsuario id = IdentificacionUsuario.of(uuid);
        assertNotNull(id);
        assertEquals(uuid.toString(), id.valor());
    }
}
