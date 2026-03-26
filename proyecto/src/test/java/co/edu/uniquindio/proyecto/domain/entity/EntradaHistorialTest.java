package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.enums.AccionHistorial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

class EntradaHistorialTest {

    @Test
    void crearEntradaHistorialValida() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        Instant fecha = Instant.now();
        
        EntradaHistorial eh = new EntradaHistorial(
            id, 
            fecha, 
            AccionHistorial.REGISTRAR_SOLICITUD, 
            usuarioId, 
            "Usuario Test", 
            "Observacion"
        );
        
        assertNotNull(eh.id());
        assertEquals(id, eh.id());
        assertEquals(AccionHistorial.REGISTRAR_SOLICITUD, eh.accion());
        assertEquals(usuarioId, eh.usuarioId());
        assertEquals("Usuario Test", eh.nombreUsuario());
        assertEquals("Observacion", eh.observacion());
    }
}
