package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueObject.*;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.*;
import co.edu.uniquindio.proyecto.domain.exception.BusinessRuleViolation;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    @Test
    void crearSolicitudValida() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        assertNotNull(s);
        assertEquals(EstadoSolicitud.REGISTRADA, s.estado());
    }

    @Test
    void crearSolicitud_solicitanteNulo() {
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertThrows(DomainException.class, () -> 
                Solicitud.crear(null, CanalOrigen.CSU, desc));
    }

    @Test
    void crearSolicitud_canalNulo() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertThrows(DomainException.class, () -> 
                Solicitud.crear(solicitante, null, desc));
    }

    @Test
    void estadoInicial_registrada() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        assertEquals(EstadoSolicitud.REGISTRADA, s.estado());
    }

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

    @Test
    void clasificar_estadoIncorrecto() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        assertThrows(BusinessRuleViolation.class, () -> 
                s.clasificar(TipoSolicitud.REGISTRO_ASIGNATURAS, coordinador));
    }

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

    @Test
    void asignarResponsable_estadoClasificada() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        Email emailDocente = new Email("docente@uniquindio.edu.co");
        Usuario docente = Usuario.crear("Docente", Rol.DOCENTE, emailDocente);
        
        s.asignarResponsable(docente, coordinador);
        assertEquals(EstadoSolicitud.EN_ATENCION, s.estado());
        assertNotNull(s.responsable());
    }

    @Test
    void asignarResponsable_usuarioInactivo() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        Email emailDocente = new Email("docente2@uniquindio.edu.co");
        Usuario docente = Usuario.crear("Docente", Rol.DOCENTE, emailDocente);
        docente.desactivar();
        
        assertThrows(BusinessRuleViolation.class, () -> 
                s.asignarResponsable(docente, coordinador));
    }

    @Test
    void asignarResponsable_estadoIncorrecto() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        
        Email emailDocente = new Email("docente3@uniquindio.edu.co");
        Usuario docente = Usuario.crear("Docente", Rol.DOCENTE, emailDocente);
        
        assertThrows(BusinessRuleViolation.class, () -> 
                s.asignarResponsable(docente, coordinador));
    }

    @Test
    void marcarAtendida_estadoEnAtencion() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        Email emailDocente = new Email("docente4@uniquindio.edu.co");
        Usuario docente = Usuario.crear("Docente", Rol.DOCENTE, emailDocente);
        
        s.asignarResponsable(docente, coordinador);
        
        UsuarioReferencia responsableRef = new UsuarioReferencia(docente.id().value(), docente.nombre());
        s.marcarAtendida(responsableRef, "Solicitud atendida");
        
        assertEquals(EstadoSolicitud.ATENDIDA, s.estado());
    }

    @Test
    void cerrar_estadoAtendida() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        Email emailDocente = new Email("docente5@uniquindio.edu.co");
        Usuario docente = Usuario.crear("Docente", Rol.DOCENTE, emailDocente);
        
        s.asignarResponsable(docente, coordinador);
        
        UsuarioReferencia responsableRef = new UsuarioReferencia(docente.id().value(), docente.nombre());
        s.marcarAtendida(responsableRef, "Solicitud atendida");
        s.cerrar(responsableRef, "Cierre de la solicitud");
        
        assertEquals(EstadoSolicitud.CERRADA, s.estado());
    }

    @Test
    void cerrar_estadoIncorrecto() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        UsuarioReferencia coordinador = new UsuarioReferencia(UUID.randomUUID(), "Coordinador");
        s.clasificar(TipoSolicitud.HOMOLOGACION, coordinador);
        
        Email emailDocente = new Email("docente6@uniquindio.edu.co");
        Usuario docente = Usuario.crear("Docente", Rol.DOCENTE, emailDocente);
        
        s.asignarResponsable(docente, coordinador);
        
        UsuarioReferencia responsableRef = new UsuarioReferencia(docente.id().value(), docente.nombre());
        
        assertThrows(BusinessRuleViolation.class, () -> 
                s.cerrar(responsableRef, "Cierre sin atender"));
    }
}
