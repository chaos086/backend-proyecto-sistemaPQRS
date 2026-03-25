package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueObject.*;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.*;
import co.edu.uniquindio.proyecto.domain.exception.BusinessRuleViolation;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    /**
     * Verifica que una solicitud se cree correctamente cuando se proporcionan
     * todos los datos requeridos.
     */
    @Test
    void crearSolicitudValida() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        assertNotNull(s);
        assertEquals(EstadoSolicitud.REGISTRADA, s.estado());
    }

    /**
     * Verifica que no sea posible crear una solicitud cuando el solicitante es nulo.
     */
    @Test
    void crearSolicitud_solicitanteNulo() {
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertThrows(DomainException.class, () -> 
                Solicitud.crear(null, CanalOrigen.CSU, desc));
    }

    /**
     * Verifica que no sea posible crear una solicitud cuando el canal de origen es nulo.
     */
    @Test
    void crearSolicitud_canalNulo() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertThrows(DomainException.class, () -> 
                Solicitud.crear(solicitante, null, desc));
    }

    /**
     * Verifica que el estado inicial de una solicitud recién creada sea REGISTRADA.
     */
    @Test
    void estadoInicial_registrada() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        assertEquals(EstadoSolicitud.REGISTRADA, s.estado());
    }

    /**
     * Verifica que una solicitud pueda ser clasificada.
     */
    @Test
    void clasificar_enEstadoRegistrada() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        assertEquals(EstadoSolicitud.CLASIFICADA, s.estado());
        assertEquals(TipoSolicitud.HOMOLOGACION, s.tipoSolicitud());
    }

    /**
     * Verifica que se pueda asignar una prioridad a una solicitud.
     */
    @Test
    void priorizar_enEstadoClasificada() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        s.priorizar(Prioridad.ALTA, new JustificacionPrioridad("Justificacion valida para la prioridad"), coordinador);
        assertEquals(Prioridad.ALTA, s.prioridad());
    }

    /**
     * Verifica que se pueda asignar un responsable a una solicitud.
     */
    @Test
    void asignarResponsable_funciona() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        UsuarioReferencia docenteRef = new UsuarioReferencia(UUID.randomUUID(), "Docente");
        s.asignarResponsable(docenteRef, coordinador);
        assertEquals(EstadoSolicitud.EN_ATENCION, s.estado());
        assertNotNull(s.responsable());
    }

    /**
     * Verifica que se pueda marcar como atendida.
     */
    @Test
    void marcarAtendida_funciona() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        UsuarioReferencia docenteRef = new UsuarioReferencia(UUID.randomUUID(), "Docente");
        s.asignarResponsable(docenteRef, coordinador);
        s.marcarAtendida(docenteRef, "Solicitud atendida");
        assertEquals(EstadoSolicitud.ATENDIDA, s.estado());
    }

    /**
     * Verifica que se pueda cerrar una solicitud.
     */
    @Test
    void cerrar_funciona() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        UsuarioReferencia docenteRef = new UsuarioReferencia(UUID.randomUUID(), "Docente");
        s.asignarResponsable(docenteRef, coordinador);
        s.marcarAtendida(docenteRef, "Solicitud atendida");
        s.cerrar(docenteRef, "Cierre de la solicitud");
        assertEquals(EstadoSolicitud.CERRADA, s.estado());
    }
}
