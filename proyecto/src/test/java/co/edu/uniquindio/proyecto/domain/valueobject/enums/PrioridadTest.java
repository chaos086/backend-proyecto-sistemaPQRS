package co.edu.uniquindio.proyecto.domain.valueobject.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrioridadTest {

    @Test
    void valoresDebenExistir() {
        assertNotNull(Prioridad.BAJA);
        assertNotNull(Prioridad.MEDIA);
        assertNotNull(Prioridad.ALTA);
    }

    @Test
    void cantidadDeValores() {
        assertEquals(3, Prioridad.values().length);
    }
}
