package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.AccionHistorial;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.JustificacionPrioridad;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Solicitud {

    private final SolicitudId id;
    private final UUID solicitanteId;
    private final String nombreSolicitante;
    private final CanalOrigen canalOrigen;
    private final Instant fechaRegistro;

    private TipoSolicitud tipoSolicitud;
    private DescripcionSolicitud descripcion;
    private Prioridad prioridad;
    private JustificacionPrioridad justificacionPrioridad;

    private EstadoSolicitud estado;
    private UUID responsableId;
    private String nombreResponsable;

    private final List<EntradaHistorial> historial = new ArrayList<>();
    private final HistorialService historialService;

    public Solicitud(SolicitudId id,
                     UUID solicitanteId,
                     String nombreSolicitante,
                     CanalOrigen canalOrigen,
                     DescripcionSolicitud descripcion,
                     HistorialService historialService) {

        if (id == null) throw new DomainException("Solicitud.id es obligatorio");
        if (solicitanteId == null) throw new DomainException("Solicitud.solicitanteId es obligatorio");
        if (nombreSolicitante == null || nombreSolicitante.isBlank()) throw new DomainException("Solicitud.nombreSolicitante es obligatorio");
        if (canalOrigen == null) throw new DomainException("Solicitud.canalOrigen es obligatorio");
        if (descripcion == null) throw new DomainException("Solicitud.descripcion es obligatoria");
        if (historialService == null) throw new DomainException("Solicitud.historialService es obligatorio");

        this.id = id;
        this.solicitanteId = solicitanteId;
        this.nombreSolicitante = nombreSolicitante;
        this.canalOrigen = canalOrigen;
        this.fechaRegistro = Instant.now();
        this.descripcion = descripcion;
        this.estado = EstadoSolicitud.REGISTRADA;
        this.historialService = historialService;

        registrarHistorial(AccionHistorial.REGISTRAR_SOLICITUD, solicitanteId, nombreSolicitante, "Solicitud registrada");
    }

    public static Solicitud crear(UUID solicitanteId, String nombreSolicitante, CanalOrigen canalOrigen, DescripcionSolicitud descripcion, HistorialService historialService) {
        return new Solicitud(
                SolicitudId.newId(),
                solicitanteId,
                nombreSolicitante,
                canalOrigen,
                descripcion,
                historialService
        );
    }

    public void clasificar(TipoSolicitud tipo, UUID coordinadorId, String nombreCoordinador) {
        if (tipo == null) throw new DomainException("TipoSolicitud es obligatorio");
        if (coordinadorId == null) throw new DomainException("CoordinadorId es obligatorio");
        if (nombreCoordinador == null || nombreCoordinador.isBlank()) throw new DomainException("Nombre del coordinador es obligatorio");

        if (this.estado != EstadoSolicitud.REGISTRADA) {
            throw new DomainException("Solo se puede clasificar una solicitud en estado REGISTRADA");
        }

        this.tipoSolicitud = tipo;
        this.estado = EstadoSolicitud.CLASIFICADA;
        registrarHistorial(AccionHistorial.CLASIFICAR_SOLICITUD, coordinadorId, nombreCoordinador, "Tipo: " + tipo);
    }

    public void priorizar(Prioridad prioridad, JustificacionPrioridad justificacion, UUID coordinadorId, String nombreCoordinador) {
        if (prioridad == null) throw new DomainException("Prioridad es obligatoria");
        if (justificacion == null) throw new DomainException("Justificación es obligatoria");
        if (coordinadorId == null) throw new DomainException("CoordinadorId es obligatorio");
        if (nombreCoordinador == null || nombreCoordinador.isBlank()) throw new DomainException("Nombre del coordinador es obligatorio");

        this.prioridad = prioridad;
        this.justificacionPrioridad = justificacion;
        registrarHistorial(AccionHistorial.PRIORIZAR_SOLICITUD, coordinadorId, nombreCoordinador, "Prioridad: " + prioridad);
    }

    public void asignarResponsable(UUID responsableId, String nombreResponsable, UUID coordinadorId, String nombreCoordinador) {
        if (responsableId == null) throw new DomainException("ResponsableId es obligatorio");
        if (nombreResponsable == null || nombreResponsable.isBlank()) throw new DomainException("Nombre del responsable es obligatorio");
        if (coordinadorId == null) throw new DomainException("CoordinadorId es obligatorio");
        if (nombreCoordinador == null || nombreCoordinador.isBlank()) throw new DomainException("Nombre del coordinador es obligatorio");

        this.responsableId = responsableId;
        this.nombreResponsable = nombreResponsable;
        this.estado = EstadoSolicitud.EN_ATENCION;
        registrarHistorial(AccionHistorial.ASIGNAR_RESPONSABLE, coordinadorId, nombreCoordinador, "Responsable: " + nombreResponsable);
    }

    public void marcarAtendida(UUID responsableId, String nombreResponsable, String observacion) {
        if (responsableId == null) throw new DomainException("ResponsableId es obligatorio");
        if (nombreResponsable == null || nombreResponsable.isBlank()) throw new DomainException("Nombre del responsable es obligatorio");

        this.estado = EstadoSolicitud.ATENDIDA;
        registrarHistorial(AccionHistorial.MARCAR_ATENDIDA, responsableId, nombreResponsable, observacion == null ? "Atendida" : observacion);
    }

    public void cerrar(UUID responsableId, String nombreResponsable, String observacionCierre) {
        if (responsableId == null) throw new DomainException("ResponsableId es obligatorio");
        if (nombreResponsable == null || nombreResponsable.isBlank()) throw new DomainException("Nombre del responsable es obligatorio");
        if (observacionCierre == null || observacionCierre.isBlank())
            throw new DomainException("Observación de cierre es obligatoria");

        this.estado = EstadoSolicitud.CERRADA;
        registrarHistorial(AccionHistorial.CERRAR_SOLICITUD, responsableId, nombreResponsable, observacionCierre);
    }

    private void registrarHistorial(AccionHistorial accion, UUID usuarioId, String nombreUsuario, String observacion) {
        List<EntradaHistorial> historialActualizado = historialService.registrarAccion(
            historial, accion, usuarioId, nombreUsuario, observacion
        );
        this.historial.clear();
        this.historial.addAll(historialActualizado);
    }

    public SolicitudId id() { return id; }
    public UUID solicitanteId() { return solicitanteId; }
    public String nombreSolicitante() { return nombreSolicitante; }
    public CanalOrigen canalOrigen() { return canalOrigen; }
    public Instant fechaRegistro() { return fechaRegistro; }
    public TipoSolicitud tipoSolicitud() { return tipoSolicitud; }
    public DescripcionSolicitud descripcion() { return descripcion; }
    public Prioridad prioridad() { return prioridad; }
    public JustificacionPrioridad justificacionPrioridad() { return justificacionPrioridad; }
    public EstadoSolicitud estado() { return estado; }
    public UUID responsableId() { return responsableId; }
    public String nombreResponsable() { return nombreResponsable; }
    public List<EntradaHistorial> historial() { return List.copyOf(historial); }
}
