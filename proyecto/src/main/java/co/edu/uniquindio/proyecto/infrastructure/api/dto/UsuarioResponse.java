package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para usuarios del sistema")
public record UsuarioResponse(
        @Schema(description = "Identificador del usuario")
        String id,
        @Schema(description = "Nombre del usuario")
        String nombre,
        @Schema(description = "Rol del usuario")
        String rol,
        @Schema(description = "Correo electrónico del usuario")
        String email,
        @Schema(description = "Estado actual del usuario")
        String estado
) {}
