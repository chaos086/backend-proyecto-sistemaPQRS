package co.edu.uniquindio.proyecto.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {
    @Test
    void mensajePreservaTexto() {
        DomainException ex = assertThrows(DomainException.class, () -> {
            throw new DomainException("mensaje de prueba");
        });
        assertEquals("mensaje de prueba", ex.getMessage());
    }
}
