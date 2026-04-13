package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import java.util.List;
import java.util.UUID;

public class SolicitudDomainService {

    private static final int MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE = 5;
    private static final int MAX_SOLICITUDES_EN_ATENCION_POR_PROFESOR = 10;

    public void validarCrearSolicitud(Usuario solicitante, List<Solicitud> solicitudesExistentes) {
        if (solicitante == null) {
            throw new DomainException("El solicitante no puede ser null");
        }
        if (!solicitante.activo()) {
            throw new DomainException("El solicitante debe estar activo");
        }

        long solicitudesPendientes = solicitudesExistentes.stream()
                .filter(s -> s.solicitanteId().toString().equals(solicitante.id().valor()))
                .filter(s -> s.estado() == EstadoSolicitud.REGISTRADA || 
                             s.estado() == EstadoSolicitud.CLASIFICADA ||
                             s.estado() == EstadoSolicitud.EN_ATENCION)
                .count();

        if (solicitudesPendientes >= MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE) {
            throw new DomainException("Un solicitante no puede tener más de " + 
                    MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE + " solicitudes pendientes");
        }
    }

    public void validarClasificar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new DomainException("La solicitud no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.REGISTRADA) {
            throw new DomainException("Solo se puede clasificar una solicitud en estado REGISTRADA");
        }
    }

    public void validarPriorizar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new DomainException("La solicitud no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.CLASIFICADA) {
            throw new DomainException("Solo se puede priorizar una solicitud en estado CLASIFICADA");
        }
    }

    public void validarAsignarResponsable(Solicitud solicitud, Usuario responsable, List<Solicitud> solicitudesExistentes) {
        if (solicitud == null) {
            throw new DomainException("La solicitud no puede ser null");
        }
        if (responsable == null) {
            throw new DomainException("El responsable no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.CLASIFICADA) {
            throw new DomainException("Solo se puede asignar responsable en estado CLASIFICADA");
        }
        if (!responsable.activo()) {
            throw new DomainException("El responsable debe estar activo");
        }
        if (responsable.rol() != Rol.PROFESOR) {
            throw new DomainException("Solo un profesor puede ser asignado como responsable");
        }

        long solicitudesEnAtencion = solicitudesExistentes.stream()
                .filter(s -> s.responsableId() != null)
                .filter(s -> s.responsableId().toString().equals(responsable.id().valor()))
                .filter(s -> s.estado() == EstadoSolicitud.EN_ATENCION)
                .count();

        if (solicitudesEnAtencion >= MAX_SOLICITUDES_EN_ATENCION_POR_PROFESOR) {
            throw new DomainException("Un profesor no puede tener más de " + 
                    MAX_SOLICITUDES_EN_ATENCION_POR_PROFESOR + " solicitudes en atención");
        }
    }

    public void validarMarcarAtendida(Solicitud solicitud, UUID responsableId) {
        if (solicitud == null) {
            throw new DomainException("La solicitud no puede ser null");
        }
        if (responsableId == null) {
            throw new DomainException("El responsable no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.EN_ATENCION) {
            throw new DomainException("Solo se puede atender una solicitud en estado EN_ATENCION");
        }
        if (solicitud.responsableId() == null) {
            throw new DomainException("No se puede atender sin responsable asignado");
        }
        if (!solicitud.responsableId().equals(responsableId)) {
            throw new DomainException("Solo el responsable asignado puede marcar como atendida");
        }
    }

    public void validarCerrar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new DomainException("La solicitud no puede ser null");
        }
        if (solicitud.estado() != EstadoSolicitud.ATENDIDA) {
            throw new DomainException("Solo se puede cerrar una solicitud que haya sido ATENDIDA");
        }
    }
}
