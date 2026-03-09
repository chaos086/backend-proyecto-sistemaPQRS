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
     *
     * GIVEN: un usuario solicitante válido y una descripción válida
     * WHEN: se crea una solicitud mediante el método crear()
     * THEN: la solicitud se instancia correctamente y su estado inicial es REGISTRADA
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
     *
     * GIVEN: una descripción válida
     * WHEN: se intenta crear una solicitud con solicitante null
     * THEN: se lanza una excepción DomainException
     */
    @Test
    void crearSolicitud_solicitanteNulo() {
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        assertThrows(DomainException.class, () -> 
                Solicitud.crear(null, CanalOrigen.CSU, desc));
    }

    /**
     * Verifica que no sea posible crear una solicitud cuando el canal de origen es nulo.
     *
     * GIVEN: un solicitante válido
     * WHEN: se intenta crear una solicitud con canal null
     * THEN: se lanza una excepción DomainException
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
     *
     * GIVEN: una solicitud creada correctamente
     * WHEN: se consulta su estado
     * THEN: el estado debe ser REGISTRADA
     */
    @Test
    void estadoInicial_registrada() {
        UsuarioReferencia solicitante = new UsuarioReferencia(UUID.randomUUID(), "Solicitante");
        DescripcionSolicitud desc = new DescripcionSolicitud("Descripcion valida para la solicitud");
        Solicitud s = Solicitud.crear(solicitante, CanalOrigen.CSU, desc);
        assertEquals(EstadoSolicitud.REGISTRADA, s.estado());
    }

    /**
     * Verifica que una solicitud pueda ser clasificada cuando se encuentra
     * en estado REGISTRADA.
     *
     * GIVEN: una solicitud en estado REGISTRADA
     * WHEN: el coordinador clasifica la solicitud indicando su tipo
     * THEN: el estado cambia a CLASIFICADA y se asigna el tipo de solicitud
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
     * Verifica que una solicitud no pueda ser clasificada más de una vez.
     *
     * GIVEN: una solicitud que ya fue clasificada
     * WHEN: se intenta clasificar nuevamente
     * THEN: se lanza una excepción BusinessRuleViolation
     */
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

    /**
     * Verifica que se pueda asignar una prioridad a una solicitud
     * cuando esta se encuentra en estado CLASIFICADA.
     *
     * GIVEN: una solicitud clasificada
     * WHEN: se establece una prioridad con su justificación
     * THEN: la prioridad de la solicitud se actualiza correctamente
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
     * Verifica que se pueda asignar un responsable a una solicitud
     * cuando se encuentra en estado CLASIFICADA.
     *
     * GIVEN: una solicitud clasificada y un usuario docente activo
     * WHEN: se asigna el docente como responsable
     * THEN: la solicitud cambia al estado EN_ATENCION y el responsable queda registrado
     */
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

    /**
     * Verifica que no sea posible asignar un responsable inactivo a una solicitud.
     *
     * GIVEN: una solicitud clasificada y un docente inactivo
     * WHEN: se intenta asignar como responsable
     * THEN: se lanza una excepción BusinessRuleViolation
     */
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

    /**
     * Verifica que no se pueda asignar un responsable si la solicitud
     * no está en el estado adecuado.
     *
     * GIVEN: una solicitud en estado REGISTRADA
     * WHEN: se intenta asignar un responsable
     * THEN: se lanza una excepción BusinessRuleViolation
     */
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

    /**
     * Verifica que una solicitud pueda marcarse como atendida cuando
     * se encuentra en estado EN_ATENCION.
     *
     * GIVEN: una solicitud con responsable asignado
     * WHEN: el responsable marca la solicitud como atendida
     * THEN: el estado cambia a ATENDIDA
     */
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

    /**
     * Verifica que una solicitud pueda cerrarse después de haber sido atendida.
     *
     * GIVEN: una solicitud en estado ATENDIDA
     * WHEN: se ejecuta el cierre de la solicitud
     * THEN: el estado cambia a CERRADA
     */
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

    /**
     * Verifica que no sea posible cerrar una solicitud que no ha sido atendida.
     *
     * GIVEN: una solicitud en estado EN_ATENCION
     * WHEN: se intenta cerrar directamente
     * THEN: se lanza una excepción BusinessRuleViolation
     */
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
