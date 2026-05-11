package co.edu.uniquindio.proyecto.infrastructure.api;

import co.edu.uniquindio.proyecto.application.usecase.ActivarUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.CrearUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.DesactivarUsuarioUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ListarUsuariosUseCase;
import co.edu.uniquindio.proyecto.application.usecase.ObtenerUsuarioUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.infrastructure.api.mapper.UsuarioRestMapperImpl;
import co.edu.uniquindio.proyecto.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, UsuarioRestMapperImpl.class})
class UsuarioControllerTest {

    @Autowired
    MockMvc mockMvc;

//    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    CrearUsuarioUseCase crearUsuarioUseCase;
    @MockitoBean
    ObtenerUsuarioUseCase obtenerUsuarioUseCase;
    @MockitoBean
    ListarUsuariosUseCase listarUsuariosUseCase;
    @MockitoBean
    ActivarUsuarioUseCase activarUsuarioUseCase;
    @MockitoBean
    DesactivarUsuarioUseCase desactivarUsuarioUseCase;

    @MockitoBean
    JwtService jwtService;

    @Test
    void crearUsuario_rechazaEmailInvalido() throws Exception {
        String body = objectMapper.writeValueAsString(java.util.Map.of(
                "nombre", "Nombre válido",
                "rol", "ESTUDIANTE",
                "email", "no-es-email"
        ));
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearUsuario_devuelve201CuandoElCasoDeUsoCreaElUsuario() throws Exception {
        Usuario creado = Usuario.crear(
                "Nombre del usuario",
                Rol.ESTUDIANTE,
                Email.of("correo@uniquindio.edu.co")
        );
        when(crearUsuarioUseCase.ejecutar(any(), any(), any())).thenReturn(creado);

        String body = """
                {
                  "nombre": "Nombre del usuario",
                  "rol": "ESTUDIANTE",
                  "email": "correo@uniquindio.edu.co"
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Nombre del usuario"))
                .andExpect(jsonPath("$.rol").value("ESTUDIANTE"));
    }

    @Test
    void obtenerUsuario_devuelve404CuandoDominioNoEncuentra() throws Exception {
        when(obtenerUsuarioUseCase.ejecutar(any(UUID.class)))
                .thenThrow(new DomainException("Usuario no encontrado"));

        mockMvc.perform(get("/api/usuarios/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }
}