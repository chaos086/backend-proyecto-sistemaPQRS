package co.edu.uniquindio.proyecto.domain.valueobject.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanalOrigenTest {

    @Test
    void valoresDebenExistir() {
        assertNotNull(CanalOrigen.PRESENCIAL);
        assertNotNull(CanalOrigen.TELEFONICO);
        assertNotNull(CanalOrigen.CORREO_ELECTRONICO);
        assertNotNull(CanalOrigen.APLICACION_WEB);
        assertNotNull(CanalOrigen.APLICACION_MOVIL);
    }

    @Test
    void cantidadDeValores() {
        assertEquals(5, CanalOrigen.values().length);
    }
}
