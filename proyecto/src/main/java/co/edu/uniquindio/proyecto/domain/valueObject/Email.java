package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.regex.Pattern;

public record Email(String valor) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public static Email of(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException("El formato del email es inválido");
        }
        return new Email(email);
    }
    
    @JsonIgnore
    public String getValor() {
        return valor;
    }
}