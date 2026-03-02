package co.edu.uniquindio.proyecto.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessRuleViolationTest {
    @Test
    void mensajePreservaTexto() {
        BusinessRuleViolation ex = assertThrows(BusinessRuleViolation.class, () -> {
            throw new BusinessRuleViolation("Regla violada");
        });
        assertEquals("Regla violada", ex.getMessage());
    }
}
