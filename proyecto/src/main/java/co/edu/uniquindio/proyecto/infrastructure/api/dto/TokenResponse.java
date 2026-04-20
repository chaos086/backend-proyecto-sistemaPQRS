package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT de acceso (Bearer)")
public record TokenResponse(
        @Schema(description = "JWT compacto firmado")
        String accessToken,
        @Schema(description = "Esquema HTTP Authorization", example = "Bearer")
        String tokenType,
        @Schema(description = "Segundos hasta exp (coincide con claim exp del JWT)")
        long expiresInSeconds
) {}
