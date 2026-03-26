package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import java.util.regex.Pattern;

public record Email(String valor) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public Email {
        if (valor == null || valor.isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (!EMAIL_PATTERN.matcher(valor).matches()) {
            throw new DomainException("El formato del email es inválido");
        }
    }
    public static Email of(String email) {
        return new Email(email);
    }
}
