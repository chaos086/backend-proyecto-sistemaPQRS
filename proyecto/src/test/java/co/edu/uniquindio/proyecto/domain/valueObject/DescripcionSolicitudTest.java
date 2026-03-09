package co.edu.uniquindio.proyecto.domain.valueObject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DescripcionSolicitudTest {

    /**
     * Verifica que una descripción válida pueda crearse correctamente.
     *
     * GIVEN: un texto de descripción que cumple con las reglas del dominio
     * WHEN: se crea una instancia de DescripcionSolicitud
     * THEN: el valor almacenado coincide con el texto proporcionado
     */
    @Test
    void descripcionValida() {
        DescripcionSolicitud d = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertEquals("Descripcion valida para la solicitud", d.value());
    }

    /**
     * Verifica que no sea posible crear una descripción vacía.
     *
     * GIVEN: una cadena vacía como descripción
     * WHEN: se intenta crear una instancia de DescripcionSolicitud
     * THEN: se lanza una DomainException indicando que la descripción no es válida
     */
    @Test
    void descripcionVaciaLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new DescripcionSolicitud(""));
    }

    /**
     * Verifica que no se permita crear una descripción con menos
     * de la longitud mínima requerida por el dominio.
     *
     * GIVEN: una descripción con menos de 10 caracteres
     * WHEN: se intenta crear una instancia de DescripcionSolicitud
     * THEN: se lanza una DomainException indicando que la descripción es demasiado corta
     */
    @Test
    void descripcionConMenosDiezLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new DescripcionSolicitud("Corta"));
    }

    /**
     * Verifica que no se permita crear una descripción que supere
     * la longitud máxima permitida por el dominio.
     *
     * GIVEN: una descripción con más de 1000 caracteres
     * WHEN: se intenta crear una instancia de DescripcionSolicitud
     * THEN: se lanza una DomainException indicando que la descripción es demasiado larga
     */
    @Test
    void descripcionConMasDeMilLanzaExcepcion() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 1001; i++) s.append('a');
        assertThrows(DomainException.class, () -> new DescripcionSolicitud(s.toString()));
    }
}
