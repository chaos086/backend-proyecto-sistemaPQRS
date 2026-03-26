package co.edu.uniquindio.proyecto.domain.valueobject.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void valoresDebenExistir() {
        assertNotNull(Rol.ESTUDIANTE);
        assertNotNull(Rol.PROFESOR);
        assertNotNull(Rol.ADMINISTRATIVO);
        assertNotNull(Rol.COORDINADOR);
    }

    @Test
    void cantidadDeValores() {
        assertEquals(4, Rol.values().length);
    }
}
