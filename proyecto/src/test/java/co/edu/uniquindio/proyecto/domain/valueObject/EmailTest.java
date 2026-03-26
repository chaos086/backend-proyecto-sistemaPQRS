package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void crearEmailValido() {
        Email email = Email.of("test@uniquindio.edu.co");
        assertNotNull(email);
        assertEquals("test@uniquindio.edu.co", email.valor());
    }

    @Test
    void crearEmailNuloDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> Email.of(null));
    }

    @Test
    void crearEmailVacioDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> Email.of(""));
    }

    @Test
    void crearEmailSinArrobaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> Email.of("testuniquindio.edu.co"));
    }

    @Test
    void crearEmailSinDominioDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> Email.of("test@"));
    }

    @Test
    void crearEmailConEspaciosDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> Email.of(" test@uniquindio.edu.co "));
    }
}
