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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

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

    @Nested
    @DisplayName("POST /api/usuarios")
    class CrearUsuario {

        @TestFactory
        Stream<DynamicTest> cuerpoInvalido_rechazaCon400() {
            return Stream.of(
                    DynamicTest.dynamicTest("email inválido",
                            () -> mockMvc.perform(post("/api/usuarios")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(Map.of(
                                                    "nombre", "Nombre válido",
                                                    "rol", "ESTUDIANTE",
                                                    "email", "no-es-email"))))
                                    .andExpect(status().isBadRequest())),
                    DynamicTest.dynamicTest("nombre vacío",
                            () -> mockMvc.perform(post("/api/usuarios")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(Map.of(
                                                    "nombre", "",
                                                    "rol", "ESTUDIANTE",
                                                    "email", "correo@uniquindio.edu.co"))))
                                    .andExpect(status().isBadRequest()))
            );
        }

        @Test
        void datosValidos_devuelve201() throws Exception {
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
    }

    @Nested
    @DisplayName("GET /api/usuarios/{id}")
    class ObtenerUsuario {

        @Test
        void usuarioNoEncontrado_devuelve404() throws Exception {
            when(obtenerUsuarioUseCase.ejecutar(any(UUID.class)))
                    .thenThrow(new DomainException("Usuario no encontrado"));

            mockMvc.perform(get("/api/usuarios/{id}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
        }
    }
}
