package co.edu.uniquindio.proyecto.domain.valueObject;

/**
 * Value Object para representar la identificación de un solicitante externo.
 * 
 * Esta clase es para soportar personas externas (no registradas en el sistema).
 * Temporalmente no se estaba utilizando en ninguna parte del código, sin embargo,
 * eso no quita el hecho de una posible utilización para implementar el soporte de externos.
 *
 * 
 * TODO: Activar cuando se implemente soporte para solicitantes externos
 */
public record IdentificacionSolicitante(String value) {

    public IdentificacionSolicitante {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("La identificación del solicitante es obligatoria");
        }
    }
}