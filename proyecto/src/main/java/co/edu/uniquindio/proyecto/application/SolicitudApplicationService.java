package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.JustificacionPrioridad;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SolicitudApplicationService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDomainService domainService;
    private final HistorialService historialService;

    public SolicitudApplicationService(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            SolicitudDomainService domainService,
            HistorialService historialService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.domainService = domainService;
        this.historialService = historialService;
    }

    public Solicitud crearSolicitud(UUID solicitanteId, String nombreSolicitante, 
                                     CanalOrigen canalOrigen, String descripcion) {
        IdentificacionUsuario idSolicitante = IdentificacionUsuario.of(solicitanteId);
        Usuario solicitante = usuarioRepository.buscarPorId(idSolicitante)
                .orElseThrow(() -> new DomainException("Solicitante no encontrado"));

        List<Solicitud> solicitudesExistentes = solicitudRepository.buscarTodas();
        domainService.validarCrearSolicitud(solicitante, solicitudesExistentes);

        DescripcionSolicitud descripcionVO = new DescripcionSolicitud(descripcion);

        Solicitud solicitud = Solicitud.crear(solicitanteId, nombreSolicitante, canalOrigen, descripcionVO, historialService);

        return solicitudRepository.guardar(solicitud);
    }

    public Solicitud clasificarSolicitud(UUID solicitudId, TipoSolicitud tipo, UUID coordinadorId) {
        Solicitud solicitud = obtenerSolicitud(solicitudId);
        domainService.validarClasificar(solicitud);
        
        Usuario coordinador = obtenerUsuario(coordinadorId);
        solicitud.clasificar(tipo, coordinadorId, coordinador.nombre());
        return solicitudRepository.guardar(solicitud);
    }

    public Solicitud priorizarSolicitud(UUID solicitudId, Prioridad prioridad, 
                                        String justificacion, UUID coordinadorId) {
        Solicitud solicitud = obtenerSolicitud(solicitudId);
        domainService.validarPriorizar(solicitud);
        
        Usuario coordinador = obtenerUsuario(coordinadorId);
        JustificacionPrioridad justificacionVO = new JustificacionPrioridad(justificacion);
        solicitud.priorizar(prioridad, justificacionVO, coordinadorId, coordinador.nombre());
        return solicitudRepository.guardar(solicitud);
    }

    public Solicitud asignarResponsable(UUID solicitudId, UUID responsableId, UUID coordinatorId) {
        Solicitud solicitud = obtenerSolicitud(solicitudId);
        
        IdentificacionUsuario idResponsable = IdentificacionUsuario.of(responsableId);
        Usuario responsable = usuarioRepository.buscarPorId(idResponsable)
                .orElseThrow(() -> new DomainException("Responsable no encontrado"));
        
        List<Solicitud> solicitudesExistentes = solicitudRepository.buscarTodas();
        domainService.validarAsignarResponsable(solicitud, responsable, solicitudesExistentes);
        
        Usuario coordinador = obtenerUsuario(coordinatorId);
        solicitud.asignarResponsable(responsableId, responsable.nombre(), coordinatorId, coordinador.nombre());
        return solicitudRepository.guardar(solicitud);
    }

    public Solicitud marcarAtendida(UUID solicitudId, UUID responsableId, String observacion) {
        Solicitud solicitud = obtenerSolicitud(solicitudId);
        domainService.validarMarcarAtendida(solicitud, responsableId);
        
        Usuario responsable = obtenerUsuario(responsableId);
        solicitud.marcarAtendida(responsableId, responsable.nombre(), observacion);
        return solicitudRepository.guardar(solicitud);
    }

    public Solicitud cerrarSolicitud(UUID solicitudId, UUID responsableId, String observacionCierre) {
        Solicitud solicitud = obtenerSolicitud(solicitudId);
        domainService.validarCerrar(solicitud);
        
        Usuario responsable = obtenerUsuario(responsableId);
        solicitud.cerrar(responsableId, responsable.nombre(), observacionCierre);
        return solicitudRepository.guardar(solicitud);
    }

    public Solicitud obtenerSolicitud(UUID solicitudId) {
        SolicitudId id = SolicitudId.of(solicitudId);
        return solicitudRepository.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Solicitud no encontrada"));
    }

    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.buscarTodas();
    }

    public List<Solicitud> listarSolicitudesPorSolicitante(UUID solicitanteId) {
        return solicitudRepository.buscarPorSolicitanteId(solicitanteId);
    }

    private Usuario obtenerUsuario(UUID usuarioId) {
        IdentificacionUsuario id = IdentificacionUsuario.of(usuarioId);
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Usuario no encontrado"));
    }
}