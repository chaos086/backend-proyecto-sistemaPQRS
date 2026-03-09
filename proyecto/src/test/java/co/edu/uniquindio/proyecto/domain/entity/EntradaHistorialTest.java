package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueObject.UsuarioReferencia;
import co.edu.uniquindio.proyecto.domain.valueObject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.AccionHistorial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

class EntradaHistorialTest {

    /**
     * Verifica que una entrada de historial se cree correctamente cuando
     * se proporcionan todos los datos requeridos.
     *
     * GIVEN: un usuario responsable válido, una acción de historial
     * y una observación asociada
     * WHEN: se crea una nueva instancia de EntradaHistorial
     * THEN:
     *  - la entrada tiene un identificador válido
     *  - la acción registrada corresponde a la proporcionada
     *  - el usuario responsable se almacena correctamente
     *  - la observación se guarda correctamente
     */
    @Test
    void crearEntradaHistorialValida() {
        UsuarioReferencia usuario = new UsuarioReferencia(IdentificacionUsuario.newId().value(), "Solicitante");
        EntradaHistorial eh = new EntradaHistorial(UUID.randomUUID(), Instant.now(), AccionHistorial.REGISTRAR_SOLICITUD, usuario, "Observacion");
        assertNotNull(eh.id());
        assertEquals(AccionHistorial.REGISTRAR_SOLICITUD, eh.accion());
        assertEquals(usuario, eh.usuarioResponsable());
        assertEquals("Observacion", eh.observacion());
    }
}
