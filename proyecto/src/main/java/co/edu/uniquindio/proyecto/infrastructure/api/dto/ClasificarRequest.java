package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para clasificar una solicitud")
public record ClasificarRequest(
    
    @NotNull(message = "El tipo de solicitud es obligatorio")
    @Schema(description = "Tipo de solicitud", example = "QUEJA")
    String tipo,
    
    @NotNull(message = "El ID del coordinador es obligatorio")
    @Schema(description = "UUID del coordinador que clasifica", example = "550e8400-e29b-41d4-a716-446655440000")
    String coordinadorId
) {}
