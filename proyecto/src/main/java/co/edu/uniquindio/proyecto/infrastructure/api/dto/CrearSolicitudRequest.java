package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para crear una nueva solicitud")
public record CrearSolicitudRequest(
    
    @NotNull(message = "El ID del solicitante es obligatorio")
    @Schema(description = "UUID del solicitante", example = "550e8400-e29b-41d4-a716-446655440000")
    String solicitanteId,
    
    @NotBlank(message = "El nombre del solicitante es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre del solicitante", example = "Juan Perez")
    String nombreSolicitante,
    
    @NotBlank(message = "El canal de origen es obligatorio")
    @Schema(description = "Canal de origen de la solicitud", example = "PRESENCIAL")
    String canalOrigen,
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    @Schema(description = "Descripción de la solicitud", example = "Solicito información sobre...")
    String descripcion
) {}
