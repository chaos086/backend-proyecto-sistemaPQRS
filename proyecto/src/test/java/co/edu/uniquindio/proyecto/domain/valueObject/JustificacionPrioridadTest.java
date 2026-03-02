package co.edu.uniquindio.proyecto.domain.valueObject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JustificacionPrioridadTest {
    @Test
    void justificacionValida() {
        JustificacionPrioridad j = new JustificacionPrioridad("Justificación válida para la prioridad");
        assertEquals("Justificación válida para la prioridad", j.value());
    }

    @Test
    void justificacionNulaLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new JustificacionPrioridad(null));
    }

    @Test
    void justificacionConMenosDiezLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new JustificacionPrioridad("Corta"));
    }
}
