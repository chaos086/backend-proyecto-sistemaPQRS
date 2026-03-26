package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;

public record JustificacionPrioridad(String valor) {

    private static final int MIN_CARACTERES = 5;
    private static final int MAX_CARACTERES = 300;

    public JustificacionPrioridad {
        if (valor == null || valor.isBlank()) {
            throw new DomainException("La justificación es obligatoria");
        }
        if (valor.length() < MIN_CARACTERES) {
            throw new DomainException("La justificación debe tener al menos " + MIN_CARACTERES + " caracteres");
        }
        if (valor.length() > MAX_CARACTERES) {
            throw new DomainException("La justificación no puede exceder " + MAX_CARACTERES + " caracteres");
        }
    }

    public static JustificacionPrioridad of(String justificacion) {
        return new JustificacionPrioridad(justificacion);
    }
}