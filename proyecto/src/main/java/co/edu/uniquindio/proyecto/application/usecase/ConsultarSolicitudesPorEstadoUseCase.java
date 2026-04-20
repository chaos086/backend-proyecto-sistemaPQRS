package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para consultar solicitudes según su estado actual.
 */
@Service
@Transactional(readOnly = true)
public class ConsultarSolicitudesPorEstadoUseCase {

    private final SolicitudRepository solicitudRepository;

    public ConsultarSolicitudesPorEstadoUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Recupera las solicitudes almacenadas con el estado indicado.
     *
     * @param estado estado por el que se desea filtrar
     * @return lista de solicitudes que coinciden con el estado
     */
    public List<Solicitud> ejecutar(EstadoSolicitud estado) {
        return solicitudRepository.buscarPorEstado(estado);
    }
}
