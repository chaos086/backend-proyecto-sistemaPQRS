package co.edu.uniquindio.proyecto.domain.valueObject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    /**
     * Verifica que un correo electrónico válido pueda crearse correctamente.
     *
     * GIVEN: una cadena que representa un correo electrónico con formato válido
     * WHEN: se crea una instancia de Email
     * THEN: el valor almacenado en el objeto coincide con el correo proporcionado
     */
    @Test
    void emailValido() {
        Email email = new Email("usuario@uniquindio.edu.co");
        assertEquals("usuario@uniquindio.edu.co", email.value());
    }

    /**
     * Verifica que no sea posible crear un Email con un valor nulo.
     *
     * GIVEN: un valor nulo como correo electrónico
     * WHEN: se intenta crear una instancia de Email
     * THEN: se lanza una DomainException indicando que el correo no es válido
     */
    @Test
    void emailNuloLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new Email((String) null));
    }

    /**
     * Verifica que no se permita crear un Email con un formato inválido.
     *
     * GIVEN: una cadena que no cumple con el formato de un correo electrónico
     * WHEN: se intenta crear una instancia de Email
     * THEN: se lanza una DomainException indicando que el formato del correo es inválido
     */
    @Test
    void emailFormatoInvalidoLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new Email("usuario-invalido"));
    }
}
