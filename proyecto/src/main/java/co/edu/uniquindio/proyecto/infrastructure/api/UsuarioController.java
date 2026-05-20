package co.edu.uniquindio.proyecto.infrastructure.api;

import co.edu.uniquindio.proyecto.application.usecase.ActivarUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.DesactivarUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ListarUsuariosUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ObtenerUsuarioUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.CrearUsuarioRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.PageResponse;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.UsuarioResponse;
import co.edu.uniquindio.proyecto.infrastructure.api.mapper.UsuarioRestMapper;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs.UsuarioEntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller para la gestión de usuarios.
 * Expone los endpoints de la API para operaciones CRUD de usuarios.
 * 
 * Endpoints disponibles:
 * - POST /api/usuarios - Crear usuario
 * - GET /api/usuarios - Listar todos los usuarios
 * - GET /api/usuarios/{id} - Obtener usuario por ID
 * - PUT /api/usuarios/{id}/activar - Activar usuario
 * - PUT /api/usuarios/{id}/desactivar - Desactivar usuario
 */
@RestController
@RequestMapping("/api/usuarios")
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios del sistema PQRS")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final ActivarUsuarioUseCase activarUsuarioUseCase;
    private final DesactivarUsuarioUseCase desactivarUsuarioUseCase;
    private final UsuarioRestMapper usuarioRestMapper;
    private final UsuarioEntityRepository usuarioEntityRepository;

    public UsuarioController(
            CrearUsuarioUseCase crearUsuarioUseCase,
            ObtenerUsuarioUseCase obtenerUsuarioUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            ActivarUsuarioUseCase activarUsuarioUseCase,
            DesactivarUsuarioUseCase desactivarUsuarioUseCase,
            UsuarioRestMapper usuarioRestMapper,
            UsuarioEntityRepository usuarioEntityRepository) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.obtenerUsuarioUseCase = obtenerUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.activarUsuarioUseCase = activarUsuarioUseCase;
        this.desactivarUsuarioUseCase = desactivarUsuarioUseCase;
        this.usuarioRestMapper = usuarioRestMapper;
        this.usuarioEntityRepository = usuarioEntityRepository;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * @param request DTO con los datos del usuario
     * @return Usuario creado
     */
    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema PQRS")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        Usuario usuario = crearUsuarioUseCase.ejecutar(
                request.nombre(),
                usuarioRestMapper.toRol(request.rol()),
                request.email()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRestMapper.toResponse(usuario));
    }

    /**
     * Lista todos los usuarios registrados en el sistema.
     * @return Lista de usuarios
     */
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados en el sistema")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRestMapper.toResponseList(listarUsuariosUseCase.ejecutar()));
    }

    /**
     * Lista los usuarios con paginaci\u00F3n server-side.
     * @param page n\u00FAmero de p\u00E1gina (default 0)
     * @param size tama\u00F1o de p\u00E1gina (default 10)
     * @return P\u00E1gina de usuarios con metadatos
     */
    @GetMapping("/page")
    @Operation(summary = "Listar usuarios paginados", description = "Obtiene usuarios con paginaci\u00F3n server-side")
    public ResponseEntity<PageResponse<UsuarioResponse>> listarUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var all = usuarioRestMapper.toResponseList(listarUsuariosUseCase.ejecutar());
        int total = all.size();
        int from = Math.min(page * size, total);
        int to = Math.min(from + size, total);
        List<UsuarioResponse> content = all.subList(from, to);
        int totalPages = (int) Math.ceil((double) total / size);
        return ResponseEntity.ok(new PageResponse<>(
                content, total, totalPages, size, page,
                page == 0, page >= totalPages - 1, total == 0));
    }

    /**
     * Activa un usuario existente.
     * @param id UUID del usuario a activar
     */
    @PutMapping("/{id}/activar")
    @Operation(summary = "Activar usuario", description = "Activa un usuario existente en el sistema")
    public ResponseEntity<Void> activarUsuario(@PathVariable UUID id) {
        activarUsuarioUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Desactiva un usuario existente.
     * @param id UUID del usuario a desactivar
     */
    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario existente en el sistema")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable UUID id) {
        desactivarUsuarioUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene un usuario por su identificador.
     * @param id UUID del usuario
     * @return Usuario encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por su identificador")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable UUID id) {
        Usuario usuario = obtenerUsuarioUseCase.ejecutar(id);
        return ResponseEntity.ok(usuarioRestMapper.toResponse(usuario));
    }
}
