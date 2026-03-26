package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.EntradaHistorial;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.AccionHistorial;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistorialService {

    public EntradaHistorial crearEntrada(AccionHistorial accion, UUID usuarioId, String nombreUsuario, String observacion) {
        return new EntradaHistorial(
            UUID.randomUUID(),
            Instant.now(),
            accion,
            usuarioId,
            nombreUsuario,
            observacion
        );
    }

    public List<EntradaHistorial> registrarAccion(List<EntradaHistorial> historial, AccionHistorial accion, UUID usuarioId, String nombreUsuario, String observacion) {
        List<EntradaHistorial> historialActualizado = new ArrayList<>(historial);
        historialActualizado.add(crearEntrada(accion, usuarioId, nombreUsuario, observacion));
        return historialActualizado;
    }
}
