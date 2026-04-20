package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Caso de uso encargado de clasificar una solicitud registrada.
 */
@Service
@Transactional
public class ClasificarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDomainService solicitudDomainService;

    public ClasificarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            SolicitudDomainService solicitudDomainService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudDomainService = solicitudDomainService;
    }

    /**
     * Ejecuta la clasificación de una solicitud.
     *
     * @param solicitudId identificador de la solicitud
     * @param tipo tipo asignado a la solicitud
     * @param coordinadorId identificador del coordinador que clasifica
     * @return solicitud actualizada y almacenada
     */
    public Solicitud ejecutar(UUID solicitudId, TipoSolicitud tipo, UUID coordinadorId) {
        Solicitud solicitud = solicitudRepository.buscarPorId(SolicitudId.of(solicitudId))
                .orElseThrow(() -> new DomainException("Solicitud no encontrada"));

        Usuario coordinador = usuarioRepository.buscarPorId(IdentificacionUsuario.of(coordinadorId))
                .orElseThrow(() -> new DomainException("Coordinador no encontrado"));

        solicitudDomainService.validarClasificar(solicitud);
        solicitud.clasificar(tipo, coordinadorId, coordinador.nombre());

        return solicitudRepository.guardar(solicitud);
    }
}
