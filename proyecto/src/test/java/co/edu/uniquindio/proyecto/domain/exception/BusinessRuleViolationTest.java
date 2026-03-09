package co.edu.uniquindio.proyecto.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessRuleViolationTest {

    /**
     * Verifica que la excepción BusinessRuleViolation preserve correctamente
     * el mensaje proporcionado al momento de su creación.
     *
     * GIVEN: un mensaje de error asociado a una violación de regla de negocio
     * WHEN: se lanza una instancia de BusinessRuleViolation con dicho mensaje
     * THEN: el mensaje almacenado en la excepción coincide con el proporcionado
     */
    @Test
    void mensajePreservaTexto() {
        BusinessRuleViolation ex = assertThrows(BusinessRuleViolation.class, () -> {
            throw new BusinessRuleViolation("Regla violada");
        });
        assertEquals("Regla violada", ex.getMessage());
    }
}
