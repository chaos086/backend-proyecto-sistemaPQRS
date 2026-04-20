package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pqrs_historial_entradas")
public class HistorialEntradaEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudEntity solicitud;

    @Column(nullable = false)
    private Instant fechaHora;

    @Column(nullable = false, length = 64)
    private String accion;

    @Column(columnDefinition = "UUID", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false, length = 200)
    private String nombreUsuario;

    @Column(length = 2000)
    private String observacion;

    protected HistorialEntradaEntity() {}

    public HistorialEntradaEntity(
            UUID id,
            SolicitudEntity solicitud,
            Instant fechaHora,
            String accion,
            UUID usuarioId,
            String nombreUsuario,
            String observacion) {
        this.id = id;
        this.solicitud = solicitud;
        this.fechaHora = fechaHora;
        this.accion = accion;
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.observacion = observacion != null ? observacion : "";
    }

    public UUID getId() {
        return id;
    }

    public SolicitudEntity getSolicitud() {
        return solicitud;
    }

    public Instant getFechaHora() {
        return fechaHora;
    }

    public String getAccion() {
        return accion;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getObservacion() {
        return observacion;
    }

    void setSolicitud(SolicitudEntity solicitud) {
        this.solicitud = solicitud;
    }
}
