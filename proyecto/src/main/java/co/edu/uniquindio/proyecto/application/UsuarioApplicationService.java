package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioApplicationService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioApplicationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario crearUsuario(String nombre, Rol rol, String email) {
        Email emailVO = new Email(email);
        Usuario usuario = Usuario.crear(nombre, rol, emailVO);
        return usuarioRepository.guardar(usuario);
    }

    public Usuario obtenerUsuario(IdentificacionUsuario id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.buscarTodas();
    }

    public void desactivarUsuario(IdentificacionUsuario id) {
        Usuario usuario = obtenerUsuario(id);
        usuario.desactivar();
        usuarioRepository.guardar(usuario);
    }

    public void activarUsuario(IdentificacionUsuario id) {
        Usuario usuario = obtenerUsuario(id);
        usuario.activar();
        usuarioRepository.guardar(usuario);
    }
}