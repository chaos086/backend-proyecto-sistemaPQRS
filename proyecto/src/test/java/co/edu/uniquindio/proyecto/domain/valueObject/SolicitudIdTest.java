package co.edu.uniquindio.proyecto.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudIdTest {

    @Test
    void crearSolicitudIdValido() {
        SolicitudId id = SolicitudId.of("123e4567-e89b-12d3-a456-426614174000");
        assertNotNull(id);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", id.valor());
    }

    @Test
    void crearSolicitudIdNuloDebeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> SolicitudId.of((String) null));
    }

    @Test
    void crearSolicitudIdVacioDebeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> SolicitudId.of(""));
    }

    @Test
    void crearSolicitudIdNewId() {
        SolicitudId id = SolicitudId.newId();
        assertNotNull(id);
        assertNotNull(id.valor());
        assertTrue(id.valor().length() > 0);
    }

    @Test
    void crearSolicitudIdFromUUID() {
        UUID uuid = UUID.randomUUID();
        SolicitudId id = SolicitudId.of(uuid);
        assertNotNull(id);
        assertEquals(uuid.toString(), id.valor());
    }
}
