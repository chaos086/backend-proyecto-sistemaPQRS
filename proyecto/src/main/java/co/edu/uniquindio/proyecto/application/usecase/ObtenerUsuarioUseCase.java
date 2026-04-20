package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Caso de uso para consultar un usuario por su identificador.
 */
@Service
@Transactional(readOnly = true)
public class ObtenerUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public ObtenerUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Recupera un usuario por identificador.
     *
     * @param usuarioId identificador del usuario
     * @return usuario encontrado
     */
    public Usuario ejecutar(UUID usuarioId) {
        return usuarioRepository.buscarPorId(IdentificacionUsuario.of(usuarioId))
                .orElseThrow(() -> new DomainException("Usuario no encontrado"));
    }
}
