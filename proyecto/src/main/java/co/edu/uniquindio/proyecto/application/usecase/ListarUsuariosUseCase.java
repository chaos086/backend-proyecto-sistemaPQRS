package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para recuperar los usuarios registrados.
 */
@Service
@Transactional(readOnly = true)
public class ListarUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Recupera todos los usuarios disponibles.
     *
     * @return lista de usuarios
     */
    public List<Usuario> ejecutar() {
        return usuarioRepository.buscarTodas();
    }
}
