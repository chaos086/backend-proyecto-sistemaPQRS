package co.edu.uniquindio.proyecto.infrastructure.api;

import co.edu.uniquindio.proyecto.application.SolicitudApplicationService;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller para la gestión de solicitudes (PQRS).
 * Expone los endpoints de la API para el ciclo de vida completo de una solicitud.
 * 
 * Endpoints disponibles:
 * - POST /api/solicitudes - Crear nueva solicitud
 * - GET /api/solicitudes - Listar todas las solicitudes
 * - GET /api/solicitudes/{id} - Obtener solicitud por ID
 * - GET /api/solicitudes/solicitante/{id} - Listar solicitudes de un solicitante
 * - PUT /api/solicitudes/{id}/clasificar - Clasificar solicitud (coordinador)
 * - PUT /api/solicitudes/{id}/priorizar - Priorizar solicitud (coordinador)
 * - PUT /api/solicitudes/{id}/asignar-responsable - Asignar responsable (coordinador)
 * - PUT /api/solicitudes/{id}/atender - Marcar como atendida (profesor)
 * - PUT /api/solicitudes/{id}/cerrar - Cerrar solicitud (profesor)
 */
@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes", description = "API para la gestión del ciclo de vida de solicitudes PQRS")
public class SolicitudController {

    private final SolicitudApplicationService solicitudService;

    public SolicitudController(SolicitudApplicationService solicitudService) {
        this.solicitudService = solicitudService;
    }

    /**
     * Crea una nueva solicitud en el sistema.
     * @param request DTO con los datos de la solicitud
     * @return Solicitud creada
     */
    @PostMapping
    @Operation(summary = "Crear solicitud", description = "Crea una nueva solicitud PQRS en el sistema")
    public ResponseEntity<Solicitud> crearSolicitud(@Valid @RequestBody CrearSolicitudRequest request) {
        UUID solicitanteId = UUID.fromString(request.solicitanteId());
        CanalOrigen canalOrigen = CanalOrigen.valueOf(request.canalOrigen().toUpperCase());

        Solicitud solicitud = solicitudService.crearSolicitud(
                solicitanteId, request.nombreSolicitante(), canalOrigen, request.descripcion()
        );
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Lista todas las solicitudes del sistema.
     * @return Lista de solicitudes
     */
    @GetMapping
    @Operation(summary = "Listar solicitudes", description = "Obtiene todas las solicitudes del sistema")
    public ResponseEntity<List<Solicitud>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudService.listarSolicitudes());
    }

    /**
     * Lista las solicitudes de un solicitante específico.
     * @param solicitanteId UUID del solicitante
     * @return Lista de solicitudes del solicitante
     */
    @GetMapping("/solicitante/{solicitanteId}")
    @Operation(summary = "Listar por solicitante", description = "Obtiene las solicitudes de un solicitante específico")
    public ResponseEntity<List<Solicitud>> listarPorSolicitante(@PathVariable UUID solicitanteId) {
        return ResponseEntity.ok(solicitudService.listarSolicitudesPorSolicitante(solicitanteId));
    }

    /**
     * Clasifica una solicitud con un tipo específico.
     * Solo aplicable cuando la solicitud está en estado REGISTRADA.
     * @param id UUID de la solicitud
     * @param request DTO con los datos de clasificación
     * @return Solicitud actualizada
     */
    @PutMapping("/{id}/clasificar")
    @Operation(summary = "Clasificar solicitud", description = "Clasifica una solicitud con un tipo específico (coordinador)")
    public ResponseEntity<Solicitud> clasificar(@PathVariable UUID id, @Valid @RequestBody ClasificarRequest request) {
        TipoSolicitud tipo = TipoSolicitud.valueOf(request.tipo().toUpperCase());
        UUID coordinadorId = UUID.fromString(request.coordinadorId());
        Solicitud solicitud = solicitudService.clasificarSolicitud(id, tipo, coordinadorId);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Asigna una prioridad a la solicitud.
     * Solo aplicable cuando la solicitud está en estado CLASIFICADA.
     * @param id UUID de la solicitud
     * @param request DTO con los datos de priorización
     * @return Solicitud actualizada
     */
    @PutMapping("/{id}/priorizar")
    @Operation(summary = "Priorizar solicitud", description = "Asigna una prioridad a la solicitud (coordinador)")
    public ResponseEntity<Solicitud> priorizar(@PathVariable UUID id, @Valid @RequestBody PriorizarRequest request) {
        Prioridad prioridad = Prioridad.valueOf(request.prioridad().toUpperCase());
        UUID coordinadorId = UUID.fromString(request.coordinadorId());
        Solicitud solicitud = solicitudService.priorizarSolicitud(id, prioridad, request.justificacion(), coordinadorId);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Asigna un profesor como responsable de la solicitud.
     * Solo aplicable cuando la solicitud está en estado CLASIFICADA.
     * @param id UUID de la solicitud
     * @param request DTO con los datos de asignación
     * @return Solicitud actualizada
     */
    @PutMapping("/{id}/asignar-responsable")
    @Operation(summary = "Asignar responsable", description = "Asigna un profesor como responsable de la solicitud (coordinador)")
    public ResponseEntity<Solicitud> asignarResponsable(@PathVariable UUID id, @Valid @RequestBody AsignarResponsableRequest request) {
        UUID responsableId = UUID.fromString(request.responsableId());
        UUID coordinadorId = UUID.fromString(request.coordinadorId());

        Solicitud solicitud = solicitudService.asignarResponsable(id, responsableId, coordinadorId);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Marca la solicitud como atendida.
     * Solo aplicable cuando la solicitud está en estado EN_ATENCION.
     * @param id UUID de la solicitud
     * @param request DTO con los datos de atención
     * @return Solicitud actualizada
     */
    @PutMapping("/{id}/atender")
    @Operation(summary = "Marcar atendida", description = "Marca la solicitud como atendida (profesor)")
    public ResponseEntity<Solicitud> marcarAtendida(@PathVariable UUID id, @Valid @RequestBody AtenderRequest request) {
        UUID responsableId = UUID.fromString(request.responsableId());

        Solicitud solicitud = solicitudService.marcarAtendida(id, responsableId, request.observacion());
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Cierra la solicitud con una observación final.
     * Solo aplicable cuando la solicitud está en estado ATENDIDA.
     * @param id UUID de la solicitud
     * @param request DTO con los datos de cierre
     * @return Solicitud actualizada
     */
    @PutMapping("/{id}/cerrar")
    @Operation(summary = "Cerrar solicitud", description = "Cierra la solicitud con una observación final (profesor)")
    public ResponseEntity<Solicitud> cerrar(@PathVariable UUID id, @Valid @RequestBody CerrarRequest request) {
        UUID responsableId = UUID.fromString(request.responsableId());

        Solicitud solicitud = solicitudService.cerrarSolicitud(id, responsableId, request.observacionCierre());
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Obtiene una solicitud por su identificador.
     * @param id UUID de la solicitud
     * @return Solicitud encontrada
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud", description = "Obtiene una solicitud por su identificador")
    public ResponseEntity<Solicitud> obtenerSolicitud(@PathVariable UUID id) {
        Solicitud solicitud = solicitudService.obtenerSolicitud(id);
        return ResponseEntity.ok(solicitud);
    }
}
