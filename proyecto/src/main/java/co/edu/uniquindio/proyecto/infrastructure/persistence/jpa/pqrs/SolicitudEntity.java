package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs;

import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA para solicitudes PQRS y su historial asociado.
 */
@Entity
@Table(name = "pqrs_solicitudes")
public class SolicitudEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "solicitante_id", columnDefinition = "UUID", nullable = false)
    private UUID solicitanteId;

    @Column(name = "nombre_solicitante", nullable = false, length = 200)
    private String nombreSolicitante;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_origen", nullable = false, length = 32)
    private CanalOrigen canalOrigen;

    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_solicitud", length = 32)
    private TipoSolicitud tipoSolicitud;

    @Column(name = "descripcion_texto", nullable = false, length = 2000)
    private String descripcionTexto;

    @Enumerated(EnumType.STRING)
    @Column(length = 24)
    private Prioridad prioridad;

    @Column(name = "justificacion_texto", length = 1000)
    private String justificacionTexto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EstadoSolicitud estado;

    @Column(name = "responsable_id", columnDefinition = "UUID")
    private UUID responsableId;

    @Column(name = "nombre_responsable", length = 200)
    private String nombreResponsable;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("fechaHora ASC")
    private List<HistorialEntradaEntity> historialEntradas = new ArrayList<>();

    protected SolicitudEntity() {}

    public SolicitudEntity(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public UUID getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(UUID solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public CanalOrigen getCanalOrigen() {
        return canalOrigen;
    }

    public void setCanalOrigen(CanalOrigen canalOrigen) {
        this.canalOrigen = canalOrigen;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getDescripcionTexto() {
        return descripcionTexto;
    }

    public void setDescripcionTexto(String descripcionTexto) {
        this.descripcionTexto = descripcionTexto;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public String getJustificacionTexto() {
        return justificacionTexto;
    }

    public void setJustificacionTexto(String justificacionTexto) {
        this.justificacionTexto = justificacionTexto;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public UUID getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(UUID responsableId) {
        this.responsableId = responsableId;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public List<HistorialEntradaEntity> getHistorialEntradas() {
        return historialEntradas;
    }

    public void reemplazarHistorial(List<HistorialEntradaEntity> nuevas) {
        historialEntradas.clear();
        for (HistorialEntradaEntity h : nuevas) {
            h.setSolicitud(this);
            historialEntradas.add(h);
        }
    }
}
