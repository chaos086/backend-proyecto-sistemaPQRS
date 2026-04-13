package co.edu.uniquindio.proyecto.infrastructure.persistence;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySolicitudRepository implements SolicitudRepository {

    private final ConcurrentMap<String, Solicitud> storage = new ConcurrentHashMap<>();

    @Override
    public Solicitud save(Solicitud solicitud) {
        storage.put(solicitud.id().valor(), solicitud);
        return solicitud;
    }

    @Override
    public Optional<Solicitud> findById(SolicitudId id) {
        return Optional.ofNullable(storage.get(id.valor()));
    }

    @Override
    public List<Solicitud> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Solicitud> findBySolicitanteId(UUID solicitanteId) {
        return storage.values().stream()
                .filter(s -> s.solicitanteId().equals(solicitanteId))
                .toList();
    }

    @Override
    public void delete(Solicitud solicitud) {
        storage.remove(solicitud.id().valor());
    }

    @Override
    public boolean existsById(SolicitudId id) {
        return storage.containsKey(id.valor());
    }
}
