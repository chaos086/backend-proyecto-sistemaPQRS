package co.edu.uniquindio.proyecto.infrastructure.api.mapper;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.UsuarioResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper manual para convertir entre contratos REST y objetos del dominio
 * asociados a los usuarios.
 */
@Component
public class UsuarioRestMapper {

    public Rol toRol(String value) {
        return Rol.valueOf(value.toUpperCase());
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.id().valor(),
                usuario.nombre(),
                usuario.rol().name(),
                usuario.email().valor(),
                usuario.estado().name()
        );
    }

    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        return usuarios.stream().map(this::toResponse).toList();
    }
}
