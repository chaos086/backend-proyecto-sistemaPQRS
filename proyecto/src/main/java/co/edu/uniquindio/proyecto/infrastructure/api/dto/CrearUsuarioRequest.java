package co.edu.uniquindio.proyecto.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para crear un nuevo usuario")
public record CrearUsuarioRequest(
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre del usuario", example = "Juan Perez")
    String nombre,
    
    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario", example = "ESTUDIANTE")
    String rol,
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    @Schema(description = "Correo electrónico del usuario", example = "juan@uniquindio.edu.co")
    String email
) {}
