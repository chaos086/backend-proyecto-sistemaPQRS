package co.edu.uniquindio.proyecto.domain.valueobject.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoSolicitudTest {

    @Test
    void valoresDebenExistir() {
        assertNotNull(EstadoSolicitud.REGISTRADA);
        assertNotNull(EstadoSolicitud.CLASIFICADA);
        assertNotNull(EstadoSolicitud.EN_ATENCION);
        assertNotNull(EstadoSolicitud.ATENDIDA);
        assertNotNull(EstadoSolicitud.CERRADA);
    }

    @Test
    void cantidadDeValores() {
        assertEquals(5, EstadoSolicitud.values().length);
    }
}
