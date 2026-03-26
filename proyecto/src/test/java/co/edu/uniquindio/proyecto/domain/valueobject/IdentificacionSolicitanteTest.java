package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentificacionSolicitanteTest {

    @Test
    void crearIdentificacionValida() {
        IdentificacionSolicitante id = IdentificacionSolicitante.of("1098765432");
        assertNotNull(id);
        assertEquals("1098765432", id.valor());
    }

    @Test
    void crearIdentificacionNulaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> IdentificacionSolicitante.of(null));
    }

    @Test
    void crearIdentificacionVaciaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> IdentificacionSolicitante.of(""));
    }

    @Test
    void crearIdentificacionEnBlancoDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> IdentificacionSolicitante.of("   "));
    }
}
