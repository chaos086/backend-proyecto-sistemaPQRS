package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para recuperar los usuarios registrados.
 */
@Service
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
