package co.edu.uniquindio.proyecto.domain.valueObject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdentificacionUsuarioTest {

    /**
     * Verifica que se genere correctamente un identificador de usuario.
     *
     * GIVEN: la solicitud de crear un nuevo identificador de usuario
     * WHEN: se invoca el método newId() de IdentificacionUsuario
     * THEN:
     *  - se obtiene una instancia válida de IdentificacionUsuario
     *  - el valor interno del identificador no es nulo
     */
    @Test
    void nuevoIdNoEsNull() {
        IdentificacionUsuario id = IdentificacionUsuario.newId();
        assertNotNull(id);
        assertNotNull(id.value());
    }
}
