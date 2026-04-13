package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para asignar un responsable a una solicitud")
public record AsignarResponsableRequest(
    
    @NotNull(message = "El ID del responsable es obligatorio")
    @Schema(description = "UUID del profesor responsable", example = "550e8400-e29b-41d4-a716-446655440000")
    String responsableId,
    
    @NotNull(message = "El ID del coordinador es obligatorio")
    @Schema(description = "UUID del coordinador que asigna", example = "550e8400-e29b-41d4-a716-446655440001")
    String coordinadorId
) {}
