package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.infrastructure.persistence.PersistenceProfiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del contrato de dominio {@link UsuarioRepository}.
 */
@Repository
@Profile(PersistenceProfiles.JPA)
@Transactional
public class JpaUsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioEntityRepository usuarioEntityRepository;

    public JpaUsuarioRepositoryAdapter(UsuarioEntityRepository usuarioEntityRepository) {
        this.usuarioEntityRepository = usuarioEntityRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = usuarioEntityRepository.findById(usuario.id().valor())
                .orElseGet(() -> new UsuarioEntity(
                        usuario.id().valor(),
                        usuario.nombre(),
                        usuario.rol(),
                        usuario.estado(),
                        usuario.email().valor()));
        entity.setNombre(usuario.nombre());
        entity.setRol(usuario.rol());
        entity.setEstado(usuario.estado());
        entity.setEmail(usuario.email().valor());
        usuarioEntityRepository.save(entity);
        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(IdentificacionUsuario id) {
        return usuarioEntityRepository.findById(id.valor()).map(this::aDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodas() {
        return usuarioEntityRepository.findAll().stream().map(this::aDominio).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorEstado(EstadoUsuario estado) {
        return usuarioEntityRepository.findByEstado(estado).stream().map(this::aDominio).toList();
    }

    private Usuario aDominio(UsuarioEntity e) {
        return new Usuario(
                IdentificacionUsuario.of(e.getId()),
                e.getNombre(),
                e.getRol(),
                e.getEstado(),
                Email.of(e.getEmail()));
    }
}
