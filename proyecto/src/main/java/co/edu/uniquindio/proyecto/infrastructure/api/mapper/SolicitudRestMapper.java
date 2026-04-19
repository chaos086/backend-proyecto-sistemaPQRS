package co.edu.uniquindio.proyecto.infrastructure.api.mapper;

import co.edu.uniquindio.proyecto.domain.entity.EntradaHistorial;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.EntradaHistorialResponse;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.SolicitudResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Mapper manual para convertir entre contratos REST y objetos del dominio
 * asociados a las solicitudes.
 */
@Component
public class SolicitudRestMapper {

    public UUID toUuid(String value) {
        return UUID.fromString(value);
    }

    public CanalOrigen toCanalOrigen(String value) {
        return CanalOrigen.valueOf(value.toUpperCase());
    }

    public TipoSolicitud toTipoSolicitud(String value) {
        return TipoSolicitud.valueOf(value.toUpperCase());
    }

    public Prioridad toPrioridad(String value) {
        return Prioridad.valueOf(value.toUpperCase());
    }

    public EstadoSolicitud toEstadoSolicitud(String value) {
        return EstadoSolicitud.valueOf(value.toUpperCase());
    }

    public SolicitudResponse toResponse(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.id().valor(),
                solicitud.solicitanteId().toString(),
                solicitud.nombreSolicitante(),
                solicitud.canalOrigen().name(),
                solicitud.fechaRegistro(),
                solicitud.tipoSolicitud() == null ? null : solicitud.tipoSolicitud().name(),
                solicitud.descripcion() == null ? null : solicitud.descripcion().valor(),
                solicitud.prioridad() == null ? null : solicitud.prioridad().name(),
                solicitud.justificacionPrioridad() == null ? null : solicitud.justificacionPrioridad().valor(),
                solicitud.estado().name(),
                solicitud.responsableId() == null ? null : solicitud.responsableId().toString(),
                solicitud.nombreResponsable(),
                solicitud.historial().stream().map(this::toResponse).toList()
        );
    }

    public List<SolicitudResponse> toResponseList(List<Solicitud> solicitudes) {
        return solicitudes.stream().map(this::toResponse).toList();
    }

    public List<EntradaHistorialResponse> toHistorialResponseList(List<EntradaHistorial> historial) {
        return historial.stream().map(this::toResponse).toList();
    }

    private EntradaHistorialResponse toResponse(EntradaHistorial entradaHistorial) {
        return new EntradaHistorialResponse(
                entradaHistorial.id().toString(),
                entradaHistorial.fechaHora(),
                entradaHistorial.accion().name(),
                entradaHistorial.usuarioId().toString(),
                entradaHistorial.nombreUsuario(),
                entradaHistorial.observacion()
        );
    }
}
