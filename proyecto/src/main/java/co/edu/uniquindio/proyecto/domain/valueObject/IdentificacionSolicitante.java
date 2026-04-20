package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record IdentificacionSolicitante(String valor) {

    public IdentificacionSolicitante {
        // Validation moved to of() method for Swagger compatibility
    }

    public static IdentificacionSolicitante of(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new DomainException("La identificación del solicitante es obligatoria");
        }
        return new IdentificacionSolicitante(identificacion);
    }
    
    @JsonIgnore
    public String getValor() {
        return valor;
    }
}