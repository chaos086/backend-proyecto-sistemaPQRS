package co.edu.uniquindio.proyecto.domain.valueObject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DescripcionSolicitudTest {
    @Test
    void descripcionValida() {
        DescripcionSolicitud d = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertEquals("Descripcion valida para la solicitud", d.value());
    }

    @Test
    void descripcionVaciaLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new DescripcionSolicitud(""));
    }

    @Test
    void descripcionConMenosDiezLanzaExcepcion() {
        assertThrows(DomainException.class, () -> new DescripcionSolicitud("Corta"));
    }

    @Test
    void descripcionConMasDeMilLanzaExcepcion() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 1001; i++) s.append('a');
        assertThrows(DomainException.class, () -> new DescripcionSolicitud(s.toString()));
    }
}
