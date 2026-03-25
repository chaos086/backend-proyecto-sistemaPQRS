package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueObject.*;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una solicitud (PQR) en el sistema.
 * Es la raíz del agregado de Solicitud.
 * 
 * Ciclo de vida de una solicitud:
 * 1. REGISTRADA - Cuando se crea la solicitud
 * 2. CLASIFICADA - Cuando el coordinador define el tipo
 * 3. EN_ATENCION - Cuando hay un docente responsable asignado
 * 4. ATENDIDA - Cuando el docente marca como atendida
 * 5. CERRADA - Cuando se completa el proceso
 * 
 * NOTA: Las validaciones de reglas de negocio están en el Domain Service.
 * Esta clase SOLO ejecuta las operaciones.
 */
public class Solicitud {

    private final SolicitudId id;
    private final UsuarioReferencia solicitante;
    private final CanalOrigen canalOrigen;
    private final Instant fechaRegistro;

    private TipoSolicitud tipoSolicitud;
    private DescripcionSolicitud descripcion;
    private Prioridad prioridad;
    private JustificacionPrioridad justificacionPrioridad;

    private EstadoSolicitud estado;
    private UsuarioReferencia responsable;

    private final List<EntradaHistorial> historial = new ArrayList<>();

    public Solicitud(SolicitudId id,
                     UsuarioReferencia solicitante,
                     CanalOrigen canalOrigen,
                     Instant fechaRegistro,
                     DescripcionSolicitud descripcion) {

        if (id == null) throw new DomainException("Solicitud.id es obligatorio");
        if (solicitante == null) throw new DomainException("Solicitud.solicitante es obligatorio");
        if (canalOrigen == null) throw new DomainException("Solicitud.canalOrigen es obligatorio");
        if (fechaRegistro == null) throw new DomainException("Solicitud.fechaRegistro es obligatorio");
        if (descripcion == null) throw new DomainException("Solicitud.descripcion es obligatoria");

        this.id = id;
        this.solicitante = solicitante;
        this.canalOrigen = canalOrigen;
        this.fechaRegistro = fechaRegistro;
        this.descripcion = descripcion;
        this.estado = EstadoSolicitud.REGISTRADA;

        registrarHistorial(AccionHistorial.REGISTRAR_SOLICITUD, solicitante, "Solicitud registrada");
    }

    public static Solicitud crear(UsuarioReferencia solicitante, CanalOrigen canalOrigen, DescripcionSolicitud descripcion) {
        return new Solicitud(
                SolicitudId.newId(),
                solicitante,
                canalOrigen,
                Instant.now(),
                descripcion
        );
    }

    /**
     * Clasifica la solicitud con un tipo específico.
     * Validación de estado: SOLO la Solicitud conoce su estado interno.
     */
    public void clasificar(TipoSolicitud tipo, UsuarioReferencia coordinador) {
        if (tipo == null) throw new DomainException("TipoSolicitud es obligatorio");
        if (coordinador == null) throw new DomainException("Coordinador es obligatorio");

        this.tipoSolicitud = tipo;
        this.estado = EstadoSolicitud.CLASIFICADA;
        registrarHistorial(AccionHistorial.CLASIFICAR_SOLICITUD, coordinador, "Tipo: " + tipo);
    }

    /**
     * Asigna una prioridad a la solicitud.
     */
    public void priorizar(Prioridad prioridad, JustificacionPrioridad justificacion, UsuarioReferencia coordinador) {
        if (prioridad == null) throw new DomainException("Prioridad es obligatoria");
        if (justificacion == null) throw new DomainException("Justificación es obligatoria");
        if (coordinador == null) throw new DomainException("Coordinador es obligatorio");

        this.prioridad = prioridad;
        this.justificacionPrioridad = justificacion;
        registrarHistorial(AccionHistorial.PRIORIZAR_SOLICITUD, coordinador, "Prioridad: " + prioridad);
    }

    /**
     * Asigna un docente como responsable.
     * Las validaciones de negocio (activo, rol) están en el Domain Service.
     */
    public void asignarResponsable(UsuarioReferencia responsableRef, UsuarioReferencia coordinador) {
        if (responsableRef == null) throw new DomainException("Responsable es obligatorio");
        if (coordinador == null) throw new DomainException("Coordinador es obligatorio");

        this.responsable = responsableRef;
        this.estado = EstadoSolicitud.EN_ATENCION;
        registrarHistorial(AccionHistorial.ASIGNAR_RESPONSABLE, coordinador, "Responsable: " + responsableRef.nombre());
    }

    /**
     * Marca la solicitud como atendida.
     */
    public void marcarAtendida(UsuarioReferencia responsableRef, String observacion) {
        if (responsableRef == null) throw new DomainException("Responsable es obligatorio");

        this.estado = EstadoSolicitud.ATENDIDA;
        registrarHistorial(AccionHistorial.MARCAR_ATENDIDA, responsableRef, observacion == null ? "Atendida" : observacion);
    }

    /**
     * Cierra la solicitud con una observación final.
     */
    public void cerrar(UsuarioReferencia responsable, String observacionCierre) {
        if (observacionCierre == null || observacionCierre.isBlank())
            throw new DomainException("Observación de cierre es obligatoria");

        this.estado = EstadoSolicitud.CERRADA;
        registrarHistorial(AccionHistorial.CERRAR_SOLICITUD, responsable, observacionCierre);
    }

    private void registrarHistorial(AccionHistorial accion, UsuarioReferencia usuario, String observacion) {
        historial.add(new EntradaHistorial(
                UUID.randomUUID(),
                Instant.now(),
                accion,
                usuario,
                observacion
        ));
    }

    // Getters
    public SolicitudId id() { return id; }
    public UsuarioReferencia solicitante() { return solicitante; }
    public CanalOrigen canalOrigen() { return canalOrigen; }
    public Instant fechaRegistro() { return fechaRegistro; }
    public TipoSolicitud tipoSolicitud() { return tipoSolicitud; }
    public DescripcionSolicitud descripcion() { return descripcion; }
    public Prioridad prioridad() { return prioridad; }
    public JustificacionPrioridad justificacionPrioridad() { return justificacionPrioridad; }
    public EstadoSolicitud estado() { return estado; }
    public UsuarioReferencia responsable() { return responsable; }
    public List<EntradaHistorial> historial() { return List.copyOf(historial); }
}
