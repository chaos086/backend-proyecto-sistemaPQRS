package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Respuesta estándar de error de la API")
public record ApiErrorResponse(
        @Schema(description = "Marca temporal del error", example = "2026-04-19T18:30:00Z")
        Instant timestamp,
        @Schema(description = "Código HTTP", example = "400")
        int status,
        @Schema(description = "Nombre del error HTTP", example = "Bad Request")
        String error,
        @Schema(description = "Mensaje descriptivo del error", example = "La solicitud no puede ser null")
        String message,
        @Schema(description = "Ruta solicitada", example = "/api/solicitudes")
        String path
) {}
