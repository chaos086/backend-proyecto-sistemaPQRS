package co.edu.uniquindio.proyecto.infrastructure.api.mapper;

import co.edu.uniquindio.proyecto.domain.entity.EntradaHistorial;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.EntradaHistorialResponse;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.SolicitudResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.UUID;

/**
 * Mapeo MapStruct entre solicitudes del dominio y contratos REST.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SolicitudRestMapper {

    @Mapping(source = "id.valor", target = "id")
    @Mapping(target = "tipoSolicitud",
            expression = "java(solicitud.tipoSolicitud() == null ? null : solicitud.tipoSolicitud().name())")
    @Mapping(target = "descripcion",
            expression = "java(solicitud.descripcion() == null ? null : solicitud.descripcion().valor())")
    @Mapping(target = "prioridad",
            expression = "java(solicitud.prioridad() == null ? null : solicitud.prioridad().name())")
    @Mapping(target = "justificacionPrioridad",
            expression = "java(solicitud.justificacionPrioridad() == null ? null : solicitud.justificacionPrioridad().valor())")
    SolicitudResponse toResponse(Solicitud solicitud);

    List<SolicitudResponse> toResponseList(List<Solicitud> solicitudes);

    List<EntradaHistorialResponse> toHistorialResponseList(List<EntradaHistorial> historial);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "usuarioId", target = "usuarioId")
    EntradaHistorialResponse toHistorialResponse(EntradaHistorial entrada);

    default UUID toUuid(String value) {
        return UUID.fromString(value);
    }

    default CanalOrigen toCanalOrigen(String value) {
        return CanalOrigen.valueOf(value.toUpperCase());
    }

    default TipoSolicitud toTipoSolicitud(String value) {
        return TipoSolicitud.valueOf(value.toUpperCase());
    }

    default Prioridad toPrioridad(String value) {
        return Prioridad.valueOf(value.toUpperCase());
    }

    default EstadoSolicitud toEstadoSolicitud(String value) {
        return EstadoSolicitud.valueOf(value.toUpperCase());
    }
}
