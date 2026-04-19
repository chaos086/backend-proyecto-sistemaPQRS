package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.JustificacionPrioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Caso de uso encargado de priorizar una solicitud previamente clasificada.
 */
@Service
public class PriorizarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDomainService solicitudDomainService;

    public PriorizarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            SolicitudDomainService solicitudDomainService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudDomainService = solicitudDomainService;
    }

    /**
     * Ejecuta la priorización de una solicitud.
     *
     * @param solicitudId identificador de la solicitud
     * @param prioridad prioridad asignada
     * @param justificacion justificación de la prioridad
     * @param coordinadorId identificador del coordinador que prioriza
     * @return solicitud actualizada y almacenada
     */
    public Solicitud ejecutar(UUID solicitudId, Prioridad prioridad, String justificacion, UUID coordinadorId) {
        Solicitud solicitud = solicitudRepository.buscarPorId(SolicitudId.of(solicitudId))
                .orElseThrow(() -> new DomainException("Solicitud no encontrada"));

        Usuario coordinador = usuarioRepository.buscarPorId(IdentificacionUsuario.of(coordinadorId))
                .orElseThrow(() -> new DomainException("Coordinador no encontrado"));

        solicitudDomainService.validarPriorizar(solicitud);
        solicitud.priorizar(prioridad, new JustificacionPrioridad(justificacion), coordinadorId, coordinador.nombre());

        return solicitudRepository.guardar(solicitud);
    }
}
