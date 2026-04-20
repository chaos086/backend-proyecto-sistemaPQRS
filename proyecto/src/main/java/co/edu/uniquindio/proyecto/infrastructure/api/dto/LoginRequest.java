package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales para obtener un token Bearer")
public record LoginRequest(
        @NotBlank @Email
        @Schema(example = "demo@uniquindio.edu.co")
        String email,
        @NotBlank
        @Schema(example = "Demo#12345")
        String password
) {}
