package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "DTO de respuesta para una solicitud PQRS")
public record SolicitudResponse(
        @Schema(description = "Identificador de la solicitud")
        String id,
        @Schema(description = "Identificador del solicitante")
        String solicitanteId,
        @Schema(description = "Nombre del solicitante")
        String nombreSolicitante,
        @Schema(description = "Canal de origen de la solicitud")
        String canalOrigen,
        @Schema(description = "Fecha y hora de registro")
        Instant fechaRegistro,
        @Schema(description = "Tipo de solicitud")
        String tipoSolicitud,
        @Schema(description = "Descripción de la solicitud")
        String descripcion,
        @Schema(description = "Prioridad asignada")
        String prioridad,
        @Schema(description = "Justificación de la prioridad")
        String justificacionPrioridad,
        @Schema(description = "Estado actual de la solicitud")
        String estado,
        @Schema(description = "Identificador del responsable asignado")
        String responsableId,
        @Schema(description = "Nombre del responsable asignado")
        String nombreResponsable,
        @Schema(description = "Historial auditable de la solicitud")
        List<EntradaHistorialResponse> historial
) {}
