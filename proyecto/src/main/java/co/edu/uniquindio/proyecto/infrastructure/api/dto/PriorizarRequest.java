package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para priorizar una solicitud")
public record PriorizarRequest(
    
    @NotNull(message = "La prioridad es obligatoria")
    @Schema(description = "Prioridad de la solicitud", example = "ALTA")
    String prioridad,
    
    @NotBlank(message = "La justificación es obligatoria")
    @Size(min = 5, max = 300, message = "La justificación debe tener entre 5 y 300 caracteres")
    @Schema(description = "Justificación de la prioridad", example = "Es urgente por riesgo académico")
    String justificacion,
    
    @NotNull(message = "El ID del coordinador es obligatorio")
    @Schema(description = "UUID del coordinador que prioriza", example = "550e8400-e29b-41d4-a716-446655440000")
    String coordinadorId
) {}
