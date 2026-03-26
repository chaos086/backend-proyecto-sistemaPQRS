package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JustificacionPrioridadTest {

    @Test
    void crearJustificacionValida() {
        JustificacionPrioridad just = JustificacionPrioridad.of("Justificacion valida para la prioridad");
        assertNotNull(just);
        assertEquals("Justificacion valida para la prioridad", just.valor());
    }

    @Test
    void crearJustificacionNulaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> JustificacionPrioridad.of(null));
    }

    @Test
    void crearJustificacionVaciaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> JustificacionPrioridad.of(""));
    }

    @Test
    void crearJustificacionMuyCortaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> JustificacionPrioridad.of("abcd"));
    }

    @Test
    void crearJustificacionMuyLargaDebeLanzarExcepcion() {
        String justificacionLarga = "a".repeat(301);
        assertThrows(DomainException.class, () -> JustificacionPrioridad.of(justificacionLarga));
    }

    @Test
    void crearJustificacionExactaMinima() {
        JustificacionPrioridad just = JustificacionPrioridad.of("12345");
        assertNotNull(just);
    }

    @Test
    void crearJustificacionExactaMaxima() {
        String justMax = "a".repeat(300);
        JustificacionPrioridad just = JustificacionPrioridad.of(justMax);
        assertNotNull(just);
    }
}
