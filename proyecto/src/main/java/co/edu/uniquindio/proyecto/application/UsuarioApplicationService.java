package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.infrastructure.persistence.UsuarioRepository;
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
        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerUsuario(IdentificacionUsuario id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public void desactivarUsuario(IdentificacionUsuario id) {
        Usuario usuario = obtenerUsuario(id);
        usuario.desactivar();
        usuarioRepository.save(usuario);
    }

    public void activarUsuario(IdentificacionUsuario id) {
        Usuario usuario = obtenerUsuario(id);
        usuario.activar();
        usuarioRepository.save(usuario);
    }
}
