package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para registrar usuarios del sistema.
 */
@Service
public class CrearUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Ejecuta el registro de un nuevo usuario.
     *
     * @param nombre nombre del usuario
     * @param rol rol del usuario
     * @param email correo electrónico del usuario
     * @return usuario creado y almacenado
     */
    public Usuario ejecutar(String nombre, Rol rol, String email) {
        Usuario usuario = Usuario.crear(nombre, rol, new Email(email));
        return usuarioRepository.guardar(usuario);
    }
}
