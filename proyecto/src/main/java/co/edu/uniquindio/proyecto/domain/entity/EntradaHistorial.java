package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.AccionHistorial;

import java.time.Instant;
import java.util.UUID;

public record EntradaHistorial(
    UUID id,
    Instant fechaHora,
    AccionHistorial accion,
    UUID usuarioId,
    String nombreUsuario,
    String observacion
) {
    public EntradaHistorial {
        if (id == null) throw new DomainException("Historial.id no puede ser null");
        if (fechaHora == null) throw new DomainException("Historial.fechaHora no puede ser null");
        if (accion == null) throw new DomainException("Historial.accion es obligatoria");
        if (usuarioId == null) throw new DomainException("Historial.usuarioId es obligatorio");
        if (nombreUsuario == null || nombreUsuario.isBlank()) throw new DomainException("Historial.nombreUsuario es obligatorio");
        if (observacion == null) observacion = "";
    }
}
