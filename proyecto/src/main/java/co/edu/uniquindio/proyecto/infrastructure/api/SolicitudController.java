package co.edu.uniquindio.proyecto.infrastructure.api;

import co.edu.uniquindio.proyecto.application.usecase.AsignarResponsableUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CambiarEstadoUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CerrarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ClasificarSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ConsultarSolicitudesPorEstadoUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CrearSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ListarSolicitudesPorSolicitanteUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ListarSolicitudesUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ObtenerSolicitudUseCase;
import co.edu.uniquindio.proyecto.application.usecase.PriorizarSolicitudUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.AsignarResponsableRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.AtenderRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.CerrarRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.ClasificarRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.CrearSolicitudRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.PriorizarRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.SolicitudResponse;
import co.edu.uniquindio.proyecto.infrastructure.api.mapper.SolicitudRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    private final CrearSolicitudUseCase crearSolicitudUseCase;
    private final ClasificarSolicitudUseCase clasificarSolicitudUseCase;
    private final PriorizarSolicitudUseCase priorizarSolicitudUseCase;
    private final AsignarResponsableUseCase asignarResponsableUseCase;
    private final CambiarEstadoUseCase cambiarEstadoUseCase;
    private final CerrarSolicitudUseCase cerrarSolicitudUseCase;
    private final ObtenerSolicitudUseCase obtenerSolicitudUseCase;
    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final ListarSolicitudesPorSolicitanteUseCase listarSolicitudesPorSolicitanteUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase;
    private final SolicitudRestMapper solicitudRestMapper;

    public SolicitudController(
            CrearSolicitudUseCase crearSolicitudUseCase,
            ClasificarSolicitudUseCase clasificarSolicitudUseCase,
            PriorizarSolicitudUseCase priorizarSolicitudUseCase,
            AsignarResponsableUseCase asignarResponsableUseCase,
            CambiarEstadoUseCase cambiarEstadoUseCase,
            CerrarSolicitudUseCase cerrarSolicitudUseCase,
            ObtenerSolicitudUseCase obtenerSolicitudUseCase,
            ListarSolicitudesUseCase listarSolicitudesUseCase,
            ListarSolicitudesPorSolicitanteUseCase listarSolicitudesPorSolicitanteUseCase,
            ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase,
            SolicitudRestMapper solicitudRestMapper) {
        this.crearSolicitudUseCase = crearSolicitudUseCase;
        this.clasificarSolicitudUseCase = clasificarSolicitudUseCase;
        this.priorizarSolicitudUseCase = priorizarSolicitudUseCase;
        this.asignarResponsableUseCase = asignarResponsableUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.cerrarSolicitudUseCase = cerrarSolicitudUseCase;
        this.obtenerSolicitudUseCase = obtenerSolicitudUseCase;
        this.listarSolicitudesUseCase = listarSolicitudesUseCase;
        this.listarSolicitudesPorSolicitanteUseCase = listarSolicitudesPorSolicitanteUseCase;
        this.consultarSolicitudesPorEstadoUseCase = consultarSolicitudesPorEstadoUseCase;
        this.solicitudRestMapper = solicitudRestMapper;
    }

    /**
     * Crea una nueva solicitud en el sistema.
     * @param request DTO con los datos de la solicitud
     * @return Solicitud creada
     */
    @PostMapping
    @Operation(summary = "Crear solicitud", description = "Crea una nueva solicitud PQRS en el sistema")
    public ResponseEntity<SolicitudResponse> crearSolicitud(@Valid @RequestBody CrearSolicitudRequest request) {
        Solicitud solicitud = crearSolicitudUseCase.ejecutar(
                solicitudRestMapper.toUuid(request.solicitanteId()),
                request.nombreSolicitante(),
                solicitudRestMapper.toCanalOrigen(request.canalOrigen()),
                request.descripcion()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudRestMapper.toResponse(solicitud));
    }

    /**
     * Lista todas las solicitudes del sistema.
     * @return Lista de solicitudes
     */
    @GetMapping
    @Operation(summary = "Listar solicitudes", description = "Obtiene todas las solicitudes del sistema")
    public ResponseEntity<List<SolicitudResponse>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudRestMapper.toResponseList(listarSolicitudesUseCase.ejecutar()));
    }

    /**
     * Lista las solicitudes filtradas por estado.
     * @param estado estado actual de la solicitud
     * @return Lista de solicitudes filtradas
     */
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar por estado", description = "Obtiene las solicitudes filtradas por estado")
    public ResponseEntity<List<SolicitudResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(
                solicitudRestMapper.toResponseList(
                        consultarSolicitudesPorEstadoUseCase.ejecutar(solicitudRestMapper.toEstadoSolicitud(estado))
                )
        );
    }

    /**
     * Lista las solicitudes de un solicitante específico.
     * @param solicitanteId UUID del solicitante
     * @return Lista de solicitudes del solicitante
     */
    @GetMapping("/solicitante/{solicitanteId}")
    @Operation(summary = "Listar por solicitante", description = "Obtiene las solicitudes de un solicitante específico")
    public ResponseEntity<List<SolicitudResponse>> listarPorSolicitante(@PathVariable UUID solicitanteId) {
        return ResponseEntity.ok(
                solicitudRestMapper.toResponseList(listarSolicitudesPorSolicitanteUseCase.ejecutar(solicitanteId))
        );
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
    public ResponseEntity<SolicitudResponse> clasificar(@PathVariable UUID id, @Valid @RequestBody ClasificarRequest request) {
        Solicitud solicitud = clasificarSolicitudUseCase.ejecutar(
                id,
                solicitudRestMapper.toTipoSolicitud(request.tipo()),
                solicitudRestMapper.toUuid(request.coordinadorId())
        );
        return ResponseEntity.ok(solicitudRestMapper.toResponse(solicitud));
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
    public ResponseEntity<SolicitudResponse> priorizar(@PathVariable UUID id, @Valid @RequestBody PriorizarRequest request) {
        Solicitud solicitud = priorizarSolicitudUseCase.ejecutar(
                id,
                solicitudRestMapper.toPrioridad(request.prioridad()),
                request.justificacion(),
                solicitudRestMapper.toUuid(request.coordinadorId())
        );
        return ResponseEntity.ok(solicitudRestMapper.toResponse(solicitud));
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
    public ResponseEntity<SolicitudResponse> asignarResponsable(@PathVariable UUID id, @Valid @RequestBody AsignarResponsableRequest request) {
        Solicitud solicitud = asignarResponsableUseCase.ejecutar(
                id,
                solicitudRestMapper.toUuid(request.responsableId()),
                solicitudRestMapper.toUuid(request.coordinadorId())
        );
        return ResponseEntity.ok(solicitudRestMapper.toResponse(solicitud));
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
    public ResponseEntity<SolicitudResponse> marcarAtendida(@PathVariable UUID id, @Valid @RequestBody AtenderRequest request) {
        Solicitud solicitud = cambiarEstadoUseCase.ejecutar(
                id,
                solicitudRestMapper.toUuid(request.responsableId()),
                request.observacion()
        );
        return ResponseEntity.ok(solicitudRestMapper.toResponse(solicitud));
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
    public ResponseEntity<SolicitudResponse> cerrar(@PathVariable UUID id, @Valid @RequestBody CerrarRequest request) {
        Solicitud solicitud = cerrarSolicitudUseCase.ejecutar(
                id,
                solicitudRestMapper.toUuid(request.responsableId()),
                request.observacionCierre()
        );
        return ResponseEntity.ok(solicitudRestMapper.toResponse(solicitud));
    }

    /**
     * Obtiene una solicitud por su identificador.
     * @param id UUID de la solicitud
     * @return Solicitud encontrada
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud", description = "Obtiene una solicitud por su identificador")
    public ResponseEntity<SolicitudResponse> obtenerSolicitud(@PathVariable UUID id) {
        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(id);
        return ResponseEntity.ok(solicitudRestMapper.toResponse(solicitud));
    }

    /**
     * Recupera el historial auditable de una solicitud específica.
     * @param id UUID de la solicitud
     * @return historial de eventos de la solicitud
     */
    @GetMapping("/{id}/historial")
    @Operation(summary = "Consultar historial", description = "Obtiene el historial auditable de una solicitud")
    public ResponseEntity<List<co.edu.uniquindio.proyecto.infrastructure.api.dto.EntradaHistorialResponse>> obtenerHistorial(@PathVariable UUID id) {
        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(id);
        return ResponseEntity.ok(solicitudRestMapper.toHistorialResponseList(solicitud.historial()));
    }
}
