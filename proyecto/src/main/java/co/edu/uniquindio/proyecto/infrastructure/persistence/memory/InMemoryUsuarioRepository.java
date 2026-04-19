package co.edu.uniquindio.proyecto.infrastructure.persistence.memory;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de usuarios para pruebas
 * manuales y escenarios simples sin base de datos.
 */
@Repository
public class InMemoryUsuarioRepository implements UsuarioRepository {

    private final List<Usuario> usuarios = new ArrayList<>();

    @Override
    public Usuario guardar(Usuario usuario) {
        buscarPorId(usuario.id()).ifPresent(usuarios::remove);
        usuarios.add(usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(IdentificacionUsuario id) {
        return usuarios.stream()
                .filter(usuario -> usuario.id().valor().equals(id.valor()))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarTodos() {
        return List.copyOf(usuarios);
    }
}
