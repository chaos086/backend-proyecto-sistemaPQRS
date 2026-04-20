package co.edu.uniquindio.proyecto.infrastructure.api;

import co.edu.uniquindio.proyecto.application.AuthLoginService;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.LoginRequest;
import co.edu.uniquindio.proyecto.infrastructure.api.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Login y token JWT (Bearer)")
public class AuthController {

    private final AuthLoginService authLoginService;

    public AuthController(AuthLoginService authLoginService) {
        this.authLoginService = authLoginService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida credenciales en base de datos y devuelve JWT temporal con expiración")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authLoginService.login(request));
    }
}
