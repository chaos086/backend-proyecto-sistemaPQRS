package co.edu.uniquindio.proyecto.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    /**
     * Verifica que la excepción DomainException conserve el mensaje
     * proporcionado durante su creación.
     *
     * GIVEN: un mensaje de error asociado a una excepción del dominio
     * WHEN: se crea y lanza una instancia de DomainException
     * THEN: el mensaje almacenado en la excepción coincide con el proporcionado
     */
    @Test
    void mensajePreservaTexto() {
        DomainException ex = assertThrows(DomainException.class, () -> {
            throw new DomainException("mensaje de prueba");
        });
        assertEquals("mensaje de prueba", ex.getMessage());
    }
}
