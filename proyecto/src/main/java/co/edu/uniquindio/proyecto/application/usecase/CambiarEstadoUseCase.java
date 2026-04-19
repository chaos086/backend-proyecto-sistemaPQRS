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

import java.util.UUID;

/**
 * Caso de uso que marca una solicitud en atención como atendida.
 * Este flujo utiliza las validaciones del dominio para garantizar que
 * solo el responsable asignado pueda completar la atención.
 */
@Service
public class CambiarEstadoUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDomainService solicitudDomainService;

    public CambiarEstadoUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            SolicitudDomainService solicitudDomainService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudDomainService = solicitudDomainService;
    }

    /**
     * Ejecuta el cambio de estado de una solicitud a ATENDIDA.
     *
     * @param solicitudId identificador de la solicitud
     * @param responsableId identificador del responsable asignado
     * @param observacion observación asociada a la atención realizada
     * @return solicitud actualizada y almacenada
     */
    public Solicitud ejecutar(UUID solicitudId, UUID responsableId, String observacion) {
        Solicitud solicitud = solicitudRepository.buscarPorId(SolicitudId.of(solicitudId))
                .orElseThrow(() -> new DomainException("Solicitud no encontrada"));

        Usuario responsable = usuarioRepository.buscarPorId(IdentificacionUsuario.of(responsableId))
                .orElseThrow(() -> new DomainException("Responsable no encontrado"));

        solicitudDomainService.validarMarcarAtendida(solicitud, responsableId);
        solicitud.marcarAtendida(responsableId, responsable.nombre(), observacion);

        return solicitudRepository.guardar(solicitud);
    }
}
