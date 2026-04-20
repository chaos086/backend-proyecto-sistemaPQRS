package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Caso de uso para desactivar un usuario existente.
 */
@Service
@Transactional
public class DesactivarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public DesactivarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Ejecuta la desactivación de un usuario.
     *
     * @param usuarioId identificador del usuario
     */
    public void ejecutar(UUID usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(IdentificacionUsuario.of(usuarioId))
                .orElseThrow(() -> new DomainException("Usuario no encontrado"));
        usuario.desactivar();
        usuarioRepository.guardar(usuario);
    }
}
