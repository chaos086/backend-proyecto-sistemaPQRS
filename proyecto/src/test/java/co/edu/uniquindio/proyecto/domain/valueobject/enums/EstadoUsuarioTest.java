package co.edu.uniquindio.proyecto.domain.valueobject.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoUsuarioTest {

    @Test
    void valoresDebenExistir() {
        assertNotNull(EstadoUsuario.ACTIVO);
        assertNotNull(EstadoUsuario.INACTIVO);
    }

    @Test
    void activoEsActivo() {
        assertEquals("ACTIVO", EstadoUsuario.ACTIVO.name());
    }

    @Test
    void inactivoEsInactivo() {
        assertEquals("INACTIVO", EstadoUsuario.INACTIVO.name());
    }

    @Test
    void cantidadDeValores() {
        assertEquals(2, EstadoUsuario.values().length);
    }
}
