package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import java.util.UUID;

public record IdentificacionUsuario(String valor) {

    public static IdentificacionUsuario of(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new DomainException("La identificación del usuario es obligatoria");
        }
        return new IdentificacionUsuario(identificacion);
    }

    public static IdentificacionUsuario of(UUID uuid) {
        return new IdentificacionUsuario(uuid.toString());
    }

    public static IdentificacionUsuario newId() {
        return new IdentificacionUsuario(UUID.randomUUID().toString());
    }
}