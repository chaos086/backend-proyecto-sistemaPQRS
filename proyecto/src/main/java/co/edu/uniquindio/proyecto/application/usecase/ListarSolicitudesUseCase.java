package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para listar todas las solicitudes registradas.
 */
@Service
public class ListarSolicitudesUseCase {

    private final SolicitudRepository solicitudRepository;

    public ListarSolicitudesUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Recupera todas las solicitudes.
     *
     * @return lista de solicitudes
     */
    public List<Solicitud> ejecutar() {
        return solicitudRepository.buscarTodas();
    }
}
