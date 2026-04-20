package co.edu.uniquindio.proyecto.infrastructure.persistence.memory;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.infrastructure.persistence.PersistenceProfiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Repository
@Profile(PersistenceProfiles.MEMORY)
public class InMemoryUsuarioRepository implements UsuarioRepository {

    private final ConcurrentMap<String, Usuario> storage = new ConcurrentHashMap<>();

    @Override
    public Usuario guardar(Usuario usuario) {
        storage.put(usuario.id().valor(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(IdentificacionUsuario id) {
        return Optional.ofNullable(storage.get(id.valor()));
    }

    @Override
    public List<Usuario> buscarTodas() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Usuario> buscarPorEstado(EstadoUsuario estado) {
        return storage.values().stream()
                .filter(usuario -> usuario.estado() == estado)
                .collect(Collectors.toList());
    }
}