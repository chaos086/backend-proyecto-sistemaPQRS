package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Caso de uso para consultar las solicitudes de un solicitante específico.
 */
@Service
public class ListarSolicitudesPorSolicitanteUseCase {

    private final SolicitudRepository solicitudRepository;

    public ListarSolicitudesPorSolicitanteUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Recupera las solicitudes creadas por un solicitante.
     *
     * @param solicitanteId identificador del solicitante
     * @return lista de solicitudes del solicitante
     */
    public List<Solicitud> ejecutar(UUID solicitanteId) {
        return solicitudRepository.buscarPorSolicitanteId(solicitanteId);
    }
}
