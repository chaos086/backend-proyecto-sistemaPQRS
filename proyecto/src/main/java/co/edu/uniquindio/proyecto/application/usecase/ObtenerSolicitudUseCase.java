package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Caso de uso para recuperar una solicitud por su identificador.
 */
@Service
@Transactional(readOnly = true)
public class ObtenerSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public ObtenerSolicitudUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Recupera una solicitud por identificador.
     *
     * @param solicitudId identificador de la solicitud
     * @return solicitud encontrada
     */
    public Solicitud ejecutar(UUID solicitudId) {
        return solicitudRepository.buscarPorId(SolicitudId.of(solicitudId))
                .orElseThrow(() -> new DomainException("Solicitud no encontrada"));
    }
}
