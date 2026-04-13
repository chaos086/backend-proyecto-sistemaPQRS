package co.edu.uniquindio.proyecto.infrastructure.api;

import co.edu.uniquindio.proyecto.application.UsuarioApplicationService;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.CrearUsuarioRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Usuarios", description = "API para la gestión de usuarios del sistema PQRS")
public class UsuarioController {

    private final UsuarioApplicationService usuarioService;

    public UsuarioController(UsuarioApplicationService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * @param request DTO con los datos del usuario
     * @return Usuario creado
     */
    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema PQRS")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        Rol rol = Rol.valueOf(request.rol().toUpperCase());

        Usuario usuario = usuarioService.crearUsuario(request.nombre(), rol, request.email());
        return ResponseEntity.ok(usuario);
    }

    /**
     * Lista todos los usuarios registrados en el sistema.
     * @return Lista de usuarios
     */
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados en el sistema")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    /**
     * Activa un usuario existente.
     * @param id UUID del usuario a activar
     */
    @PutMapping("/{id}/activar")
    @Operation(summary = "Activar usuario", description = "Activa un usuario existente en el sistema")
    public ResponseEntity<Void> activarUsuario(@PathVariable UUID id) {
        IdentificacionUsuario identificacion = IdentificacionUsuario.of(id);
        usuarioService.activarUsuario(identificacion);
        return ResponseEntity.ok().build();
    }

    /**
     * Desactiva un usuario existente.
     * @param id UUID del usuario a desactivar
     */
    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario existente en el sistema")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable UUID id) {
        IdentificacionUsuario identificacion = IdentificacionUsuario.of(id);
        usuarioService.desactivarUsuario(identificacion);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene un usuario por su identificador.
     * @param id UUID del usuario
     * @return Usuario encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por su identificador")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable UUID id) {
        IdentificacionUsuario identificacion = IdentificacionUsuario.of(id);
        Usuario usuario = usuarioService.obtenerUsuario(identificacion);
        return ResponseEntity.ok(usuario);
    }
}
