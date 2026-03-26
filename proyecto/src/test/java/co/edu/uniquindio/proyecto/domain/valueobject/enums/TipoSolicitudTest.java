package co.edu.uniquindio.proyecto.domain.valueobject.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoSolicitudTest {

    @Test
    void valoresDebenExistir() {
        assertNotNull(TipoSolicitud.QUEJA);
        assertNotNull(TipoSolicitud.RECLAMO);
        assertNotNull(TipoSolicitud.SUGERENCIA);
        assertNotNull(TipoSolicitud.PETICION);
        assertNotNull(TipoSolicitud.FELICITACION);
    }

    @Test
    void cantidadDeValores() {
        assertEquals(5, TipoSolicitud.values().length);
    }
}
