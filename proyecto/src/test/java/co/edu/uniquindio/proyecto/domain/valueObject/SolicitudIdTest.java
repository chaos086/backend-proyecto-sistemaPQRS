package co.edu.uniquindio.proyecto.domain.valueObject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudIdTest {
    @Test
    void nuevoIdNoEsNull() {
        SolicitudId id = SolicitudId.newId();
        assertNotNull(id);
        assertNotNull(id.value());
    }
}
