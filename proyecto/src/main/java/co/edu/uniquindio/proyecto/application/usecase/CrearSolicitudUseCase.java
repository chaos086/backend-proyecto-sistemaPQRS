package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Caso de uso encargado de registrar una nueva solicitud en el sistema.
 * Orquesta la consulta del solicitante, la validación de reglas del dominio
 * y la persistencia de la solicitud creada.
 */
@Service
public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDomainService solicitudDomainService;
    private final HistorialService historialService;

    public CrearSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            SolicitudDomainService solicitudDomainService,
            HistorialService historialService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudDomainService = solicitudDomainService;
        this.historialService = historialService;
    }

    /**
     * Ejecuta el flujo de creación de una solicitud.
     *
     * @param solicitanteId identificador del usuario que registra la solicitud
     * @param nombreSolicitante nombre del solicitante usado en el historial
     * @param canalOrigen canal por el cual se registra la solicitud
     * @param descripcion descripción textual del caso
     * @return solicitud creada y almacenada
     */
    public Solicitud ejecutar(
            UUID solicitanteId,
            String nombreSolicitante,
            CanalOrigen canalOrigen,
            String descripcion) {

        IdentificacionUsuario idSolicitante = IdentificacionUsuario.of(solicitanteId);

        Usuario solicitante = usuarioRepository.buscarPorId(idSolicitante)
                .orElseThrow(() -> new DomainException("Solicitante no encontrado"));

        List<Solicitud> solicitudesExistentes = solicitudRepository.buscarTodas();
        solicitudDomainService.validarCrearSolicitud(solicitante, solicitudesExistentes);

        DescripcionSolicitud descripcionVO = new DescripcionSolicitud(descripcion);
        Solicitud solicitud = Solicitud.crear(
                solicitanteId,
                nombreSolicitante,
                canalOrigen,
                descripcionVO,
                historialService
        );

        return solicitudRepository.guardar(solicitud);
    }
}
