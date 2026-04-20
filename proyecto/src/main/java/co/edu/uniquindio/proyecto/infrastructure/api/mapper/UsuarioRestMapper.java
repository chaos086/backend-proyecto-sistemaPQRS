package co.edu.uniquindio.proyecto.infrastructure.api.mapper;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapeo MapStruct entre entidad de dominio {@link Usuario} y contratos REST.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioRestMapper {

    @Mapping(source = "id.valor", target = "id")
    @Mapping(source = "email.valor", target = "email")
    UsuarioResponse toResponse(Usuario usuario);

    List<UsuarioResponse> toResponseList(List<Usuario> usuarios);

    default Rol toRol(String value) {
        return Rol.valueOf(value.toUpperCase());
    }
}
