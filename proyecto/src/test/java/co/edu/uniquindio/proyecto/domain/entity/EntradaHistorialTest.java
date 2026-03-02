package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueObject.UsuarioReferencia;
import co.edu.uniquindio.proyecto.domain.valueObject.IdentificacionUsuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import co.edu.uniquindio.proyecto.domain.valueObject.SolicitudId;

class EntradaHistorialTest {

    @Test
    void crearEntradaHistorialValida() {
        UsuarioReferencia usuario = new UsuarioReferencia(IdentificacionUsuario.newId().value(), "Solicitante");
        EntradaHistorial eh = new EntradaHistorial(UUID.randomUUID(), Instant.now(), "REGISTRAR_SOLICITUD", usuario, "Observacion");
        assertNotNull(eh.id());
        assertEquals("REGISTRAR_SOLICITUD", eh.accion());
        assertEquals(usuario, eh.usuarioResponsable());
        assertEquals("Observacion", eh.observacion());
    }
}
