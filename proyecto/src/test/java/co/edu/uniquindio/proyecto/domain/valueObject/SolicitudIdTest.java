package co.edu.uniquindio.proyecto.domain.valueObject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudIdTest {

    /**
     * Verifica que se genere correctamente un identificador único
     * para una solicitud.
     *
     * GIVEN: la solicitud de crear un nuevo identificador de solicitud
     * WHEN: se invoca el método newId() de SolicitudId
     * THEN:
     *  - se obtiene una instancia válida de SolicitudId
     *  - el valor interno del identificador no es nulo
     */
    @Test
    void nuevoIdNoEsNull() {
        SolicitudId id = SolicitudId.newId();
        assertNotNull(id);
        assertNotNull(id.value());
    }
}
