package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs;

import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA para {@link SolicitudEntity}. Incluye consultas personalizadas.
 */
public interface SolicitudEntityRepository extends JpaRepository<SolicitudEntity, String> {

    @Query("""
            SELECT DISTINCT s FROM SolicitudEntity s LEFT JOIN FETCH s.historialEntradas
            """)
    List<SolicitudEntity> findAllWithHistorial();

    @Query("""
            SELECT DISTINCT s FROM SolicitudEntity s
            LEFT JOIN FETCH s.historialEntradas WHERE s.estado = :estado
            """)
    List<SolicitudEntity> findByEstadoWithHistorial(@Param("estado") EstadoSolicitud estado);

    @Query("""
            SELECT DISTINCT s FROM SolicitudEntity s
            LEFT JOIN FETCH s.historialEntradas WHERE s.solicitanteId = :sid
            """)
    List<SolicitudEntity> findBySolicitanteIdWithHistorial(@Param("sid") java.util.UUID solicitanteId);

    List<SolicitudEntity> findByEstado(EstadoSolicitud estado);

    List<SolicitudEntity> findBySolicitanteId(java.util.UUID solicitanteId);

    @Query("""
            SELECT DISTINCT s FROM SolicitudEntity s
            LEFT JOIN FETCH s.historialEntradas
            WHERE s.id = :id
            """)
    Optional<SolicitudEntity> findByIdWithHistorial(@Param("id") String id);

    /**
     * JPQL con rango temporal (mejor que encadenar 5 predicados en el nombre del método derivado).
     */
    @Query("""
            SELECT s FROM SolicitudEntity s
            WHERE s.estado = :estado AND s.fechaRegistro >= :desde
            ORDER BY s.fechaRegistro DESC
            """)
    List<SolicitudEntity> listarPorEstadoDesde(
            @Param("estado") EstadoSolicitud estado,
            @Param("desde") Instant desde);

    @Query(value = "SELECT COUNT(*) FROM pqrs_solicitudes WHERE estado = :estado", nativeQuery = true)
    long contarPorEstadoNativo(@Param("estado") String estado);
}
