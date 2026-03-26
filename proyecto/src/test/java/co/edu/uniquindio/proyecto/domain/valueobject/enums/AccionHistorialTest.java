package co.edu.uniquindio.proyecto.domain.valueobject.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccionHistorialTest {

    @Test
    void valoresDebenExistir() {
        assertNotNull(AccionHistorial.REGISTRAR_SOLICITUD);
        assertNotNull(AccionHistorial.CLASIFICAR_SOLICITUD);
        assertNotNull(AccionHistorial.PRIORIZAR_SOLICITUD);
        assertNotNull(AccionHistorial.ASIGNAR_RESPONSABLE);
        assertNotNull(AccionHistorial.MARCAR_ATENDIDA);
        assertNotNull(AccionHistorial.CERRAR_SOLICITUD);
    }

    @Test
    void cantidadDeValores() {
        assertEquals(6, AccionHistorial.values().length);
    }
}
