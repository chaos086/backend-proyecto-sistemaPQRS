package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;

import java.util.List;
import java.util.UUID;

/**
 * Caso de uso responsable de asignar un profesor como responsable de una
 * solicitud previamente clasificada.
 */
public class AsignarResponsableUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDomainService solicitudDomainService;

    public AsignarResponsableUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            SolicitudDomainService solicitudDomainService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudDomainService = solicitudDomainService;
    }

    /**
     * Ejecuta la asignación de un responsable a una solicitud.
     *
     * @param solicitudId identificador de la solicitud a actualizar
     * @param responsableId identificador del profesor responsable
     * @param coordinadorId identificador del coordinador que realiza la acción
     * @return solicitud actualizada y almacenada
     */
    public Solicitud ejecutar(UUID solicitudId, UUID responsableId, UUID coordinadorId) {
        Solicitud solicitud = solicitudRepository.buscarPorId(SolicitudId.of(solicitudId))
                .orElseThrow(() -> new DomainException("Solicitud no encontrada"));

        Usuario responsable = usuarioRepository.buscarPorId(IdentificacionUsuario.of(responsableId))
                .orElseThrow(() -> new DomainException("Responsable no encontrado"));

        Usuario coordinador = usuarioRepository.buscarPorId(IdentificacionUsuario.of(coordinadorId))
                .orElseThrow(() -> new DomainException("Coordinador no encontrado"));

        List<Solicitud> solicitudesExistentes = solicitudRepository.buscarTodas();
        solicitudDomainService.validarAsignarResponsable(solicitud, responsable, solicitudesExistentes);

        solicitud.asignarResponsable(responsableId, responsable.nombre(), coordinadorId, coordinador.nombre());
        return solicitudRepository.guardar(solicitud);
    }
}
