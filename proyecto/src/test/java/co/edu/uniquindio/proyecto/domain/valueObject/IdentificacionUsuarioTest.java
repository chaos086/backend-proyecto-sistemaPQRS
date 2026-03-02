package co.edu.uniquindio.proyecto.domain.valueObject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdentificacionUsuarioTest {
    @Test
    void nuevoIdNoEsNull() {
        IdentificacionUsuario id = IdentificacionUsuario.newId();
        assertNotNull(id);
        assertNotNull(id.value());
    }
}
