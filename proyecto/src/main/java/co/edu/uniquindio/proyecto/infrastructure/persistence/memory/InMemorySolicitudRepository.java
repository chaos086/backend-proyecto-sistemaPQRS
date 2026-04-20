package co.edu.uniquindio.proyecto.infrastructure.persistence.memory;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.persistence.PersistenceProfiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@Profile(PersistenceProfiles.MEMORY)
public class InMemorySolicitudRepository implements SolicitudRepository {

    private final ConcurrentMap<String, Solicitud> storage = new ConcurrentHashMap<>();

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        storage.put(solicitud.id().valor(), solicitud);
        return solicitud;
    }

    @Override
    public Optional<Solicitud> buscarPorId(SolicitudId id) {
        return Optional.ofNullable(storage.get(id.valor()));
    }

    @Override
    public List<Solicitud> buscarTodas() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Solicitud> buscarPorEstado(EstadoSolicitud estado) {
        return storage.values().stream()
                .filter(s -> s.estado() == estado)
                .toList();
    }

    @Override
    public List<Solicitud> buscarPorSolicitanteId(UUID solicitanteId) {
        return storage.values().stream()
                .filter(s -> s.solicitanteId().equals(solicitanteId))
                .toList();
    }
}