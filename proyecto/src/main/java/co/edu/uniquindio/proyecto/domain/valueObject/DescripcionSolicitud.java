package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;

public record DescripcionSolicitud(String valor) {

    private static final int MIN_CARACTERES = 10;
    private static final int MAX_CARACTERES = 500;

    public DescripcionSolicitud {
        if (valor == null || valor.isBlank()) {
            throw new DomainException("La descripción es obligatoria");
        }
        if (valor.length() < MIN_CARACTERES) {
            throw new DomainException("La descripción debe tener al menos " + MIN_CARACTERES + " caracteres");
        }
        if (valor.length() > MAX_CARACTERES) {
            throw new DomainException("La descripción no puede exceder " + MAX_CARACTERES + " caracteres");
        }
    }
    public static DescripcionSolicitud of(String descripcion) {
        return new DescripcionSolicitud(descripcion);
    }
}