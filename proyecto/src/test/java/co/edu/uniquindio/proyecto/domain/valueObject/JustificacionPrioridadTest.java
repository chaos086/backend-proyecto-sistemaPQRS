package co.edu.uniquindio.proyecto.domain.valueObject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JustificacionPrioridadTest {

    /**
     * Verifica que una justificación válida para la prioridad
     * pueda crearse correctamente.
     *
     * GIVEN: un texto de justificación que cumple con las reglas del dominio
     * WHEN: se crea una instancia de JustificacionPrioridad
     * THEN: el valor almacenado coincide con el texto proporcionado
     */
    @Test
    void justificacionValida() {
        JustificacionPrioridad j = new JustificacionPrioridad("Justificación válida para la prioridad");
        assertEquals("Justificación válida para la prioridad", j.value());
    }

    /**
     * Verifica que no sea posible crear una justificación de prioridad
     * con un valor nulo.
     *
     * GIVEN: un valor nulo como justificación
     * WHEN: se intenta crear una instancia de JustificacionPrioridad
     * THEN: se lanza una DomainException indicando que la justificación es inválida
     */
    @Test
    void justificacionNulaLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new JustificacionPrioridad(null));
    }

    /**
     * Verifica que no se permita crear una justificación con menos
     * de la longitud mínima requerida por el dominio.
     *
     * GIVEN: una justificación con menos de 10 caracteres
     * WHEN: se intenta crear una instancia de JustificacionPrioridad
     * THEN: se lanza una DomainException indicando que la justificación es demasiado corta
     */
    @Test
    void justificacionConMenosDiezLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new JustificacionPrioridad("Corta"));
    }
}
