package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs;

import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA para {@link UsuarioEntity}. Incluye consultas personalizadas (JPQL / nativas).
 */
public interface UsuarioEntityRepository extends JpaRepository<UsuarioEntity, String> {

    List<UsuarioEntity> findByEstado(EstadoUsuario estado);

    /**
     * Consulta personalizada JPQL: búsqueda por fragmento de nombre (evita nombres derivados ilegibles).
     */
    @Query("""
            SELECT u FROM UsuarioEntity u
            WHERE UPPER(u.nombre) LIKE UPPER(CONCAT('%', :fragmento, '%'))
            ORDER BY u.nombre ASC
            """)
    List<UsuarioEntity> buscarPorNombreContiene(@Param("fragmento") String fragmento);

    /**
     * Nativa: agregado por rol en SQL (útil para informes / índices).
     */
    @Query(value = "SELECT COUNT(*) FROM pqrs_usuarios WHERE rol = :rol AND estado = 'ACTIVO'", nativeQuery = true)
    long contarActivosPorRolNativo(@Param("rol") String rol);

    /**
     * Derivado simple: emails de un dominio (inferencia legible).
     */
    List<UsuarioEntity> findByEmailEndingWithIgnoreCase(String dominioEmail);
}
