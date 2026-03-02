package co.edu.uniquindio.proyecto.domain.valueObject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;

/**
 * Value Object que representa un correo electrónico.
 * Valida que el formato del email sea correcto.
 * 
 * @author Sistema PQRS
 * @version 1.0
 */
public record Email(String value) {
    public Email {
        if (value == null || value.isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new DomainException("El formato del email es inválido");
        }
    }
}
