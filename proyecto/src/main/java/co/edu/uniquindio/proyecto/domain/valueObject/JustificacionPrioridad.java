package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;

public record JustificacionPrioridad(String valor) {

    private static final int MIN_CARACTERES = 5;
    private static final int MAX_CARACTERES = 300;

    public JustificacionPrioridad {
        // Validation moved to of() method for Swagger compatibility
    }

    public static JustificacionPrioridad of(String justificacion) {
        if (justificacion == null || justificacion.isBlank()) {
            throw new DomainException("La justificación es obligatoria");
        }
        if (justificacion.length() < MIN_CARACTERES) {
            throw new DomainException("La justificación debe tener al menos " + MIN_CARACTERES + " caracteres");
        }
        if (justificacion.length() > MAX_CARACTERES) {
            throw new DomainException("La justificación no puede exceder " + MAX_CARACTERES + " caracteres");
        }
        return new JustificacionPrioridad(justificacion);
    }
}