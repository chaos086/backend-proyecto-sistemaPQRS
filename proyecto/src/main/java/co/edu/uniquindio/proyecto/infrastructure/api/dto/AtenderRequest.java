package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para marcar una solicitud como atendida")
public record AtenderRequest(
    
    @NotNull(message = "El ID del responsable es obligatorio")
    @Schema(description = "UUID del responsable que marca como atendida", example = "550e8400-e29b-41d4-a716-446655440000")
    String responsableId,
    
    @Schema(description = "Observación de la atención", example = "Se respondió al estudiante sobre...")
    String observacion
) {}
