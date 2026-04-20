package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Reglas prácticas (Spring Data JPA):
 * <ul>
 *   <li><b>Nombre de inferencia limpio</b>: use métodos derivados cuando el predicado es corto y coincide
 *       con el vocabulario del modelo ({@code findByRol}, {@code existsBy…}). Son autodocumentados y Spring
 *       traduce bien a SQL con paginación.</li>
 *   <li><b>Ceder a JPQL / SQL nativo</b>: cuando el nombre derivado sería ilegible (encadenamiento profundo de
 *       VO, OR complejos, subconsultas, funciones DB). Una firma tipo
 *       {@code findByCorreoElectronicoValorIgnoreCaseAndRolInOrderByIdDesc…} debe reemplazarse por
 *       {@code @Query} con nombre explícito del caso de uso.</li>
 *   <li><b>Value Objects encajados</b>: en JPQL navegue la ruta del embeddable:
 *       {@code u.correoElectronico.valor}. En SQL nativo sigue existiendo la columna física ({@code email}).</li>
 *   <li><b>Paginación eficiente</b>: {@link Pageable} en el último parámetro; Spring inyecta {@code LIMIT}/{@code OFFSET}
 *       y, en {@code @Query}, obligue a definir {@code countQuery} para que el total no cargue todas las filas.</li>
 * </ul>
 */
public interface AuthUsuarioJpaRepository extends JpaRepository<AuthUsuarioEntity, String> {

    /**
     * SQL nativo explícito (equivalente relacional directo sobre la columna física {@code email}).
     */
    @Query(value = "SELECT * FROM auth_usuarios WHERE email = :email", nativeQuery = true)
    Optional<AuthUsuarioEntity> findByEmailNative(@Param("email") String email);

    /**
     * JPQL sobre el VO embebido: prefiera esto antes que un nombre derivado kilométrico cuando el equipo
     * prioriza legibilidad sobre convenio findBy*.
     */
    @Query("""
            SELECT u FROM AuthUsuarioEntity u
            WHERE LOWER(u.correoElectronico.valor) = LOWER(:email)
            """)
    Optional<AuthUsuarioEntity> buscarPorCorreoJpql(@Param("email") String email);

    /**
     * Inferencia corta y estable: predicado simple sobre columna plana {@code rol}.
     */
    Page<AuthUsuarioEntity> findByRol(String rol, Pageable pageable);

    /**
     * Paginación nativa + recuento separado → limitación SQL real y {@code COUNT(*)} ligero para el UI.
     */
    @Query(
            value = "SELECT * FROM auth_usuarios WHERE rol = :rol ORDER BY email ASC",
            countQuery = "SELECT COUNT(*) FROM auth_usuarios WHERE rol = :rol",
            nativeQuery = true)
    Page<AuthUsuarioEntity> buscarPorRolPaginadoNativo(@Param("rol") String rol, Pageable pageable);
}
