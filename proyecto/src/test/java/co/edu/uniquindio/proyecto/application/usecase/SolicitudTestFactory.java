package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.JustificacionPrioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;

import java.util.UUID;

/**
 * Construye {@link Solicitud} en estados conocidos para pruebas unitarias de casos de uso.
 */
public final class SolicitudTestFactory {

    private static final HistorialService HISTORIAL = new HistorialService();

    private static final DescripcionSolicitud DESC =
            DescripcionSolicitud.of("Descripción de prueba con más de diez caracteres.");

    private SolicitudTestFactory() {}

    public static Solicitud solicitudRegistrada(UUID solicitudId, UUID solicitanteId) {
        return new Solicitud(
                SolicitudId.of(solicitudId),
                solicitanteId,
                "Solicitante Prueba",
                CanalOrigen.PRESENCIAL,
                DESC,
                HISTORIAL);
    }

    public static Solicitud solicitudClasificada(UUID solicitudId, UUID solicitanteId, UUID coordinadorId) {
        Solicitud s = solicitudRegistrada(solicitudId, solicitanteId);
        s.clasificar(TipoSolicitud.PETICION, coordinadorId, "Coordinador");
        return s;
    }

    /**
     * Clasificada y priorizada (sigue en estado CLASIFICADA hasta asignar responsable).
     */
    public static Solicitud solicitudClasificadaYPriorizada(
            UUID solicitudId, UUID solicitanteId, UUID coordinadorId) {
        Solicitud s = solicitudClasificada(solicitudId, solicitanteId, coordinadorId);
        s.priorizar(Prioridad.MEDIA, JustificacionPrioridad.of("Justificación de prioridad para pruebas."), coordinadorId, "Coordinador");
        return s;
    }

    public static Solicitud solicitudEnAtencion(
            UUID solicitudId, UUID solicitanteId, UUID coordinadorId, UUID profesorId) {
        Solicitud s = solicitudClasificada(solicitudId, solicitanteId, coordinadorId);
        s.asignarResponsable(profesorId, "Profesor Responsable", coordinadorId, "Coordinador");
        return s;
    }

    public static Solicitud solicitudAtendida(
            UUID solicitudId, UUID solicitanteId, UUID coordinadorId, UUID profesorId) {
        Solicitud s = solicitudEnAtencion(solicitudId, solicitanteId, coordinadorId, profesorId);
        s.marcarAtendida(profesorId, "Profesor Responsable", "Observación suficientemente descriptiva del cierre.");
        return s;
    }
}
