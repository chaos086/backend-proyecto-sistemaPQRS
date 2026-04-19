package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrato del dominio para el acceso y almacenamiento de solicitudes.
 */
public interface SolicitudRepository {

    /**
     * Almacena una solicitud en el repositorio.
     *
     * @param solicitud solicitud a persistir
     * @return solicitud almacenada
     */
    Solicitud guardar(Solicitud solicitud);

    /**
     * Busca una solicitud por su identificador.
     *
     * @param id identificador de la solicitud
     * @return solicitud encontrada si existe
     */
    Optional<Solicitud> buscarPorId(SolicitudId id);

    /**
     * Recupera todas las solicitudes registradas.
     *
     * @return lista completa de solicitudes
     */
    List<Solicitud> buscarTodas();

    /**
     * Recupera las solicitudes que se encuentran en un estado determinado.
     *
     * @param estado estado por el que se desea filtrar
     * @return lista de solicitudes filtradas
     */
    List<Solicitud> buscarPorEstado(EstadoSolicitud estado);

    /**
     * Recupera las solicitudes registradas por un solicitante específico.
     *
     * @param solicitanteId identificador del solicitante
     * @return lista de solicitudes del solicitante
     */
    List<Solicitud> buscarPorSolicitanteId(UUID solicitanteId);
}
