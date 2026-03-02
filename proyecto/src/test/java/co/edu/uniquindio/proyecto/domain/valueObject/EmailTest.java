package co.edu.uniquindio.proyecto.domain.valueObject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    @Test
    void emailValido() {
        Email email = new Email("usuario@uniquindio.edu.co");
        assertEquals("usuario@uniquindio.edu.co", email.value());
    }

    @Test
    void emailNuloLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new Email((String) null));
    }

    @Test
    void emailFormatoInvalidoLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new Email("usuario-invalido"));
    }
}
