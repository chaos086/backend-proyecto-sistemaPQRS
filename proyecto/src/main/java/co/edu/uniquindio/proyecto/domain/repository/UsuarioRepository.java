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
    List<Usuario> buscarTodas();

    /**
     * Recupera los usuarios filtrados por estado.
     *
     * @param estado estado por el que se desea filtrar
     * @return lista de usuarios filtrados
     */
    List<Usuario> buscarPorEstado(co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario estado);
}
