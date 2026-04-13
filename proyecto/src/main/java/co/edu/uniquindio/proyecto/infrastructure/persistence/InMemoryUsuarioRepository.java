package co.edu.uniquindio.proyecto.infrastructure.persistence;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryUsuarioRepository implements UsuarioRepository {

    private final ConcurrentMap<String, Usuario> storage = new ConcurrentHashMap<>();

    @Override
    public Usuario save(Usuario usuario) {
        storage.put(usuario.id().valor(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> findById(IdentificacionUsuario id) {
        return Optional.ofNullable(storage.get(id.valor()));
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Usuario usuario) {
        storage.remove(usuario.id().valor());
    }

    @Override
    public boolean existsById(IdentificacionUsuario id) {
        return storage.containsKey(id.valor());
    }
}
