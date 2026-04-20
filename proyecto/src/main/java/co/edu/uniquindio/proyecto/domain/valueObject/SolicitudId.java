package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.UUID;

public record SolicitudId(String valor) {

    public static SolicitudId of(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de solicitud no puede ser null o vacío");
        }
        return new SolicitudId(id);
    }

    public static SolicitudId of(UUID uuid) {
        return new SolicitudId(uuid.toString());
    }

    public static SolicitudId newId() {
        return new SolicitudId(UUID.randomUUID().toString());
    }

    public String getValor() {
        return valor;
    }
}