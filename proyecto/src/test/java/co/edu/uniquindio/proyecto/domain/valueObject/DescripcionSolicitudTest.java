package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescripcionSolicitudTest {

    @Test
    void crearDescripcionValida() {
        DescripcionSolicitud desc = DescripcionSolicitud.of("Descripcion valida de la solicitud");
        assertNotNull(desc);
        assertEquals("Descripcion valida de la solicitud", desc.valor());
    }

    @Test
    void crearDescripcionNulaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> DescripcionSolicitud.of(null));
    }

    @Test
    void crearDescripcionVaciaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> DescripcionSolicitud.of(""));
    }

    @Test
    void crearDescripcionMuyCortaDebeLanzarExcepcion() {
        assertThrows(DomainException.class, () -> DescripcionSolicitud.of("Corta"));
    }

    @Test
    void crearDescripcionMuyLargaDebeLanzarExcepcion() {
        String descripcionLarga = "a".repeat(501);
        assertThrows(DomainException.class, () -> DescripcionSolicitud.of(descripcionLarga));
    }

    @Test
    void crearDescripcionExactaMinima() {
        DescripcionSolicitud desc = DescripcionSolicitud.of("1234567890");
        assertNotNull(desc);
    }

    @Test
    void crearDescripcionExactaMaxima() {
        String descripcionMax = "a".repeat(500);
        DescripcionSolicitud desc = DescripcionSolicitud.of(descripcionMax);
        assertNotNull(desc);
    }
}
