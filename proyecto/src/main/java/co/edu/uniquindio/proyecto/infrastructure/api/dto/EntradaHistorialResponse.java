package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "DTO de respuesta para una entrada del historial de la solicitud")
public record EntradaHistorialResponse(
        @Schema(description = "Identificador de la entrada de historial")
        String id,
        @Schema(description = "Fecha y hora del evento")
        Instant fechaHora,
        @Schema(description = "Acción realizada")
        String accion,
        @Schema(description = "Identificador del usuario responsable")
        String usuarioId,
        @Schema(description = "Nombre del usuario responsable")
        String nombreUsuario,
        @Schema(description = "Observación asociada al evento")
        String observacion
) {}
