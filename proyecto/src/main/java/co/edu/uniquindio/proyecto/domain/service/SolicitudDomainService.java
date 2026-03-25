package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.BusinessRuleViolation;
import co.edu.uniquindio.proyecto.domain.valueObject.UsuarioReferencia;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.Rol;
import java.util.List;

/**
 * Domain Service que encapsula las reglas de negocio del sistema de PQRS.
 * 
 * Principio DDD: Este servicio SOLO valida reglas de negocio.
 * Las operaciones las ejecutan las entidades (Solicitud).
 */
public class SolicitudDomainService {

    private static final int MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE = 5;
    private static final int MAX_SOLICITUDES_EN_ATENCION_POR_DOCENTE = 10;

    // ==================== VALIDACIONES DE CREAR SOLICITUD ====================

    /**
     * Valida que un solicitante pueda crear una nueva solicitud.
     */
    public void validarCrearSolicitud(Usuario solicitante, List<Solicitud> solicitudesExistentes) {
        if (solicitante == null) {
            throw new BusinessRuleViolation("El solicitante no puede ser null");
        }
        if (!solicitante.activo()) {
            throw new BusinessRuleViolation("El solicitante debe estar activo");
        }

        long solicitudesPendientes = solicitudesExistentes.stream()
                .filter(s -> s.solicitante().value().equals(solicitante.id().value()))
                .filter(s -> s.estado() == EstadoSolicitud.REGISTRADA || 
                             s.estado() == EstadoSolicitud.CLASIFICADA ||
                             s.estado() == EstadoSolicitud.EN_ATENCION)
                .count();

        if (solicitudesPendientes >= MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE) {
            throw new BusinessRuleViolation("Un solicitante no puede tener más de " + 
                    MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE + " solicitudes pendientes");
        }
    }

    // ==================== VALIDACIONES DE CLASIFICAR ====================

    /**
     * Valida que la solicitud pueda ser clasificada.
     * Debe estar en estado REGISTRADA.
     */
    public void validarClasificar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new BusinessRuleViolation("La solicitud no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.REGISTRADA) {
            throw new BusinessRuleViolation("Solo se puede clasificar una solicitud en estado REGISTRADA");
        }
    }

    // ==================== VALIDACIONES DE PRIORIZAR ====================

    /**
     * Valida que la solicitud pueda ser priorizada.
     * Debe estar en estado CLASIFICADA.
     */
    public void validarPriorizar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new BusinessRuleViolation("La solicitud no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.CLASIFICADA) {
            throw new BusinessRuleViolation("Solo se puede priorizar una solicitud en estado CLASIFICADA");
        }
    }

    // ==================== VALIDACIONES DE ASIGNAR RESPONSABLE ====================

    /**
     * Valida que se pueda asignar un responsable a una solicitud.
     * Verifica: estado de solicitud, que el docente esté activo, rol DOCENTE, y límite de solicitudes.
     */
    public void validarAsignarResponsable(Solicitud solicitud, Usuario responsable, List<Solicitud> solicitudesExistentes) {
        if (solicitud == null) {
            throw new BusinessRuleViolation("La solicitud no puede ser null");
        }
        if (responsable == null) {
            throw new BusinessRuleViolation("El responsable no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.CLASIFICADA) {
            throw new BusinessRuleViolation("Solo se puede asignar responsable en estado CLASIFICADA");
        }
        if (!responsable.activo()) {
            throw new BusinessRuleViolation("El responsable debe estar activo");
        }
        if (responsable.rol() != Rol.DOCENTE) {
            throw new BusinessRuleViolation("Solo un docente puede ser asignado como responsable");
        }

        long solicitudesEnAtencion = solicitudesExistentes.stream()
                .filter(s -> s.responsable() != null)
                .filter(s -> s.responsable().value().equals(responsable.id().value()))
                .filter(s -> s.estado() == EstadoSolicitud.EN_ATENCION)
                .count();

        if (solicitudesEnAtencion >= MAX_SOLICITUDES_EN_ATENCION_POR_DOCENTE) {
            throw new BusinessRuleViolation("Un docente no puede tener más de " + 
                    MAX_SOLICITUDES_EN_ATENCION_POR_DOCENTE + " solicitudes en atención");
        }
    }

    // ==================== VALIDACIONES DE MARCAR ATENDIDA ====================

    /**
     * Valida que la solicitud pueda ser marcada como atendida.
     * Verifica: estado EN_ATENCION y que el responsable sea el mismo.
     */
    public void validarMarcarAtendida(Solicitud solicitud, UsuarioReferencia responsable) {
        if (solicitud == null) {
            throw new BusinessRuleViolation("La solicitud no puede ser null");
        }
        if (responsable == null) {
            throw new BusinessRuleViolation("El responsable no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.EN_ATENCION) {
            throw new BusinessRuleViolation("Solo se puede atender una solicitud en estado EN_ATENCION");
        }
        if (solicitud.responsable() == null) {
            throw new BusinessRuleViolation("No se puede atender sin responsable asignado");
        }
        if (!solicitud.responsable().equals(responsable)) {
            throw new BusinessRuleViolation("Solo el responsable asignado puede marcar como atendida");
        }
    }

    // ==================== VALIDACIONES DE CERRAR ====================

    /**
     * Valida que la solicitud pueda ser cerrada.
     * Debe estar en estado ATENDIDA.
     */
    public void validarCerrar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new BusinessRuleViolation("La solicitud no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.ATENDIDA) {
            throw new BusinessRuleViolation("Solo se puede cerrar una solicitud que haya sido ATENDIDA");
        }
    }
}
