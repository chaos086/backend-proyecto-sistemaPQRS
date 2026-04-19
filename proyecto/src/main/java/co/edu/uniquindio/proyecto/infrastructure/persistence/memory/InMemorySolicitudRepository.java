package co.edu.uniquindio.proyecto.infrastructure.persistence.memory;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación en memoria del repositorio de solicitudes para pruebas
 * manuales y escenarios simples sin base de datos.
 */
@Repository
public class InMemorySolicitudRepository implements SolicitudRepository {

    private final List<Solicitud> solicitudes = new ArrayList<>();

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        buscarPorId(solicitud.id()).ifPresent(solicitudes::remove);
        solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public Optional<Solicitud> buscarPorId(SolicitudId id) {
        return solicitudes.stream()
                .filter(solicitud -> solicitud.id().valor().equals(id.valor()))
                .findFirst();
    }

    @Override
    public List<Solicitud> buscarTodas() {
        return List.copyOf(solicitudes);
    }

    @Override
    public List<Solicitud> buscarPorEstado(EstadoSolicitud estado) {
        return solicitudes.stream()
                .filter(solicitud -> solicitud.estado() == estado)
                .toList();
    }

    @Override
    public List<Solicitud> buscarPorSolicitanteId(UUID solicitanteId) {
        return solicitudes.stream()
                .filter(solicitud -> solicitud.solicitanteId().equals(solicitanteId))
                .toList();
    }
}
