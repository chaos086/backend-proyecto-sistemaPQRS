package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.UUID;

public record SolicitudId(String valor) {

    public SolicitudId {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El ID de solicitud no puede ser null o vacío");
        }
    }

    public static SolicitudId of(String id) {
        return new SolicitudId(id);
    }

    public static SolicitudId of(UUID uuid) {
        return new SolicitudId(uuid.toString());
    }

    public static SolicitudId newId() {
        return new SolicitudId(UUID.randomUUID().toString());
    }
}