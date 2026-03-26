package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;

public record IdentificacionSolicitante(String valor) {

    public IdentificacionSolicitante {
        if (valor == null || valor.isBlank()) {
            throw new DomainException("La identificación del solicitante es obligatoria");
        }
    }

    public static IdentificacionSolicitante of(String identificacion) {
        return new IdentificacionSolicitante(identificacion);
    }
}