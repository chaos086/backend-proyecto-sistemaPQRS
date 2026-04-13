package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para cerrar una solicitud")
public record CerrarRequest(
    
    @NotNull(message = "El ID del responsable es obligatorio")
    @Schema(description = "UUID del responsable que cierra la solicitud", example = "550e8400-e29b-41d4-a716-446655440000")
    String responsableId,
    
    @NotBlank(message = "La observación de cierre es obligatoria")
    @Size(min = 10, max = 500, message = "La observación debe tener entre 10 y 500 caracteres")
    @Schema(description = "Observación final de cierre", example = "Se resolvió satisfactoriamente...")
    String observacionCierre
) {}
