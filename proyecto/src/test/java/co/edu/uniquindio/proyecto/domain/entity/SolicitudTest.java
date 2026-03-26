package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.JustificacionPrioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    private HistorialService historialService;

    @BeforeEach
    void setUp() {
        historialService = new HistorialService();
    }

    @Test
    void crearSolicitudValida() {
        UUID solicitanteId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitanteId, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService);
        assertNotNull(s);
        assertEquals(EstadoSolicitud.REGISTRADA, s.estado());
    }

    @Test
    void crearSolicitud_solicitanteIdNulo() {
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertThrows(DomainException.class, () -> 
                Solicitud.crear(null, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService));
    }

    @Test
    void crearSolicitud_canalNulo() {
        UUID solicitanteId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertThrows(DomainException.class, () -> 
                Solicitud.crear(solicitanteId, "Solicitante", null, desc, historialService));
    }

    @Test
    void estadoInicial_registrada() {
        UUID solicitanteId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitanteId, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService);
        assertEquals(EstadoSolicitud.REGISTRADA, s.estado());
    }

    @Test
    void clasificar_enEstadoRegistrada() {
        UUID solicitanteId = UUID.randomUUID();
        UUID coordinadorId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitanteId, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService);
        s.clasificar(TipoSolicitud.PETICION, coordinadorId, "Coordinador");
        assertEquals(EstadoSolicitud.CLASIFICADA, s.estado());
        assertEquals(TipoSolicitud.PETICION, s.tipoSolicitud());
    }

    @Test
    void priorizar_enEstadoClasificada() {
        UUID solicitanteId = UUID.randomUUID();
        UUID coordinadorId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitanteId, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService);
        s.clasificar(TipoSolicitud.PETICION, coordinadorId, "Coordinador");
        s.priorizar(Prioridad.ALTA, new JustificacionPrioridad("Justificacion valida para la prioridad"), coordinadorId, "Coordinador");
        assertEquals(Prioridad.ALTA, s.prioridad());
    }

    @Test
    void asignarResponsable_funciona() {
        UUID solicitanteId = UUID.randomUUID();
        UUID coordinadorId = UUID.randomUUID();
        UUID docenteId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitanteId, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService);
        s.clasificar(TipoSolicitud.PETICION, coordinadorId, "Coordinador");
        s.asignarResponsable(docenteId, "Docente", coordinadorId, "Coordinador");
        assertEquals(EstadoSolicitud.EN_ATENCION, s.estado());
        assertNotNull(s.responsableId());
    }

    @Test
    void marcarAtendida_funciona() {
        UUID solicitanteId = UUID.randomUUID();
        UUID coordinadorId = UUID.randomUUID();
        UUID docenteId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitanteId, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService);
        s.clasificar(TipoSolicitud.PETICION, coordinadorId, "Coordinador");
        s.asignarResponsable(docenteId, "Docente", coordinadorId, "Coordinador");
        s.marcarAtendida(docenteId, "Docente", "Solicitud atendida");
        assertEquals(EstadoSolicitud.ATENDIDA, s.estado());
    }

    @Test
    void cerrar_funciona() {
        UUID solicitanteId = UUID.randomUUID();
        UUID coordinadorId = UUID.randomUUID();
        UUID docenteId = UUID.randomUUID();
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitanteId, "Solicitante", CanalOrigen.PRESENCIAL, desc, historialService);
        s.clasificar(TipoSolicitud.PETICION, coordinadorId, "Coordinador");
        s.asignarResponsable(docenteId, "Docente", coordinadorId, "Coordinador");
        s.marcarAtendida(docenteId, "Docente", "Solicitud atendida");
        s.cerrar(docenteId, "Docente", "Cierre de la solicitud");
        assertEquals(EstadoSolicitud.CERRADA, s.estado());
    }
}
