package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs;

import co.edu.uniquindio.proyecto.domain.entity.EntradaHistorial;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.JustificacionPrioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.AccionHistorial;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.persistence.PersistenceProfiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador JPA del contrato de dominio {@link SolicitudRepository}.
 */
@Repository
@Profile(PersistenceProfiles.JPA)
@Transactional
public class JpaSolicitudRepositoryAdapter implements SolicitudRepository {

    private final SolicitudEntityRepository solicitudEntityRepository;
    private final HistorialService historialService;

    public JpaSolicitudRepositoryAdapter(
            SolicitudEntityRepository solicitudEntityRepository,
            HistorialService historialService) {
        this.solicitudEntityRepository = solicitudEntityRepository;
        this.historialService = historialService;
    }

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        String sid = solicitud.id().valor();
        SolicitudEntity entity = solicitudEntityRepository.findById(sid)
                .orElseGet(() -> new SolicitudEntity(sid));
        copiarDesdeDominio(solicitud, entity);
        entity.reemplazarHistorial(mapearHistorialPersistencia(solicitud.historial(), entity));
        solicitudEntityRepository.save(entity);
        return solicitud;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Solicitud> buscarPorId(SolicitudId id) {
        return solicitudEntityRepository.findByIdWithHistorial(id.valor()).map(this::aDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarTodas() {
        return solicitudEntityRepository.findAllWithHistorial().stream().map(this::aDominio).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorEstado(EstadoSolicitud estado) {
        return solicitudEntityRepository.findByEstadoWithHistorial(estado).stream().map(this::aDominio).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorSolicitanteId(UUID solicitanteId) {
        return solicitudEntityRepository.findBySolicitanteIdWithHistorial(solicitanteId).stream()
                .map(this::aDominio).toList();
    }

    private void copiarDesdeDominio(Solicitud s, SolicitudEntity e) {
        e.setSolicitanteId(s.solicitanteId());
        e.setNombreSolicitante(s.nombreSolicitante());
        e.setCanalOrigen(s.canalOrigen());
        e.setFechaRegistro(s.fechaRegistro());
        e.setTipoSolicitud(s.tipoSolicitud());
        e.setDescripcionTexto(s.descripcion().valor());
        e.setPrioridad(s.prioridad());
        e.setJustificacionTexto(s.justificacionPrioridad() == null ? null : s.justificacionPrioridad().valor());
        e.setEstado(s.estado());
        e.setResponsableId(s.responsableId());
        e.setNombreResponsable(s.nombreResponsable());
    }

    private List<HistorialEntradaEntity> mapearHistorialPersistencia(List<EntradaHistorial> lista, SolicitudEntity padre) {
        return lista.stream()
                .map(h -> new HistorialEntradaEntity(
                        h.id(),
                        padre,
                        h.fechaHora(),
                        h.accion().name(),
                        h.usuarioId(),
                        h.nombreUsuario(),
                        h.observacion()))
                .toList();
    }

    private Solicitud aDominio(SolicitudEntity e) {
        DescripcionSolicitud desc = new DescripcionSolicitud(e.getDescripcionTexto());
        JustificacionPrioridad just = e.getJustificacionTexto() == null || e.getJustificacionTexto().isBlank()
                ? null
                : new JustificacionPrioridad(e.getJustificacionTexto());
        List<EntradaHistorial> hist = e.getHistorialEntradas().stream().map(this::aHistorialDominio).toList();
        return Solicitud.rehidratar(
                SolicitudId.of(e.getId()),
                e.getSolicitanteId(),
                e.getNombreSolicitante(),
                e.getCanalOrigen(),
                e.getFechaRegistro(),
                e.getTipoSolicitud(),
                desc,
                e.getPrioridad(),
                just,
                e.getEstado(),
                e.getResponsableId(),
                e.getNombreResponsable(),
                hist,
                historialService);
    }

    private EntradaHistorial aHistorialDominio(HistorialEntradaEntity x) {
        return EntradaHistorial.of(
                x.getId(),
                x.getFechaHora(),
                AccionHistorial.valueOf(x.getAccion()),
                x.getUsuarioId(),
                x.getNombreUsuario(),
                x.getObservacion());
    }
}
