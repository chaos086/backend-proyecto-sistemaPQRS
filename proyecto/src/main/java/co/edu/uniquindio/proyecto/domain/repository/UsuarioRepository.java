package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;

import java.util.List;
import java.util.Optional;

/**
 * Contrato del dominio para el acceso y almacenamiento de usuarios.
 */
public interface UsuarioRepository {

    /**
     * Almacena un usuario en el repositorio.
     *
     * @param usuario usuario a persistir
     * @return usuario almacenado
     */
    Usuario guardar(Usuario usuario);

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado si existe
     */
    Optional<Usuario> buscarPorId(IdentificacionUsuario id);

    /**
     * Recupera todos los usuarios registrados.
     *
     * @return lista completa de usuarios
     */
    List<Usuario> buscarTodos();
}
