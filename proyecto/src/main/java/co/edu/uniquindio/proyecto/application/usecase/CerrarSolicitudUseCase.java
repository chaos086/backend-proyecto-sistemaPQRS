package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Caso de uso encargado de cerrar una solicitud previamente atendida.
 */
@Service
@Transactional
public class CerrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDomainService solicitudDomainService;

    public CerrarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            SolicitudDomainService solicitudDomainService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudDomainService = solicitudDomainService;
    }

    /**
     * Ejecuta el cierre de una solicitud.
     *
     * @param solicitudId identificador de la solicitud
     * @param responsableId identificador del usuario que realiza el cierre
     * @param observacionCierre observación final de cierre
     * @return solicitud actualizada y almacenada
     */
    public Solicitud ejecutar(UUID solicitudId, UUID responsableId, String observacionCierre) {
        Solicitud solicitud = solicitudRepository.buscarPorId(SolicitudId.of(solicitudId))
                .orElseThrow(() -> new DomainException("Solicitud no encontrada"));

        Usuario responsable = usuarioRepository.buscarPorId(IdentificacionUsuario.of(responsableId))
                .orElseThrow(() -> new DomainException("Responsable no encontrado"));

        solicitudDomainService.validarCerrar(solicitud);
        solicitud.cerrar(responsableId, responsable.nombre(), observacionCierre);

        return solicitudRepository.guardar(solicitud);
    }
}
