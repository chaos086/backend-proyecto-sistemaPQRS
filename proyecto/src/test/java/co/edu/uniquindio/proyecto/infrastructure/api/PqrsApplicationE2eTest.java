package co.edu.uniquindio.proyecto.infrastructure.api;

import co.edu.uniquindio.proyecto.config.AuthUsuarioDataBootstrap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "jpa", inheritProfiles = false)
class PqrsApplicationE2eTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        String loginBody = """
                {"email":"%s","password":"%s"}
                """.formatted(AuthUsuarioDataBootstrap.DEMO_EMAIL, AuthUsuarioDataBootstrap.DEMO_PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        jwtToken = "Bearer " + json.get("accessToken").asText();
    }

    @Nested
    @DisplayName("Autenticación")
    class Autenticacion {

        @Test
        void loginConCredencialesInvalidas_devuelve401() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"email":"wrong@test.com","password":"badpass"}
                                    """))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
        }
    }

    @Nested
    @DisplayName("Endpoints protegidos")
    class EndpointsProtegidos {

        @Test
        void solicitudesSinToken_devuelve403() throws Exception {
            mockMvc.perform(get("/api/solicitudes"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void usuariosSinToken_devuelve403() throws Exception {
            mockMvc.perform(get("/api/usuarios"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Ciclo completo de Solicitud")
    class CicloCompletoSolicitud {

        @Test
        void flujoCrearListarYObtenerSolicitud() throws Exception {
            String crearUsuarioBody = """
                    {"nombre":"Solicitante Test","rol":"ESTUDIANTE","email":"solicitante@uniquindio.edu.co"}
                    """;
            MvcResult usuarioResult = mockMvc.perform(post("/api/usuarios")
                            .header(HttpHeaders.AUTHORIZATION, jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crearUsuarioBody))
                    .andExpect(status().isCreated())
                    .andReturn();
            JsonNode usuarioJson = objectMapper.readTree(usuarioResult.getResponse().getContentAsString());
            String solicitanteId = usuarioJson.get("id").asText();

            String crearBody = """
                    {
                      "solicitanteId": "%s",
                      "nombreSolicitante": "Solicitante Test",
                      "canalOrigen": "PRESENCIAL",
                      "descripcion": "Descripción con al menos diez caracteres válidos."
                    }
                    """.formatted(solicitanteId);

            MvcResult crearResult = mockMvc.perform(post("/api/solicitudes")
                            .header(HttpHeaders.AUTHORIZATION, jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crearBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.estado").value("REGISTRADA"))
                    .andExpect(jsonPath("$.solicitanteId").value(solicitanteId))
                    .andReturn();

            JsonNode json = objectMapper.readTree(crearResult.getResponse().getContentAsString());
            String solicitudId = json.get("id").asText();

            mockMvc.perform(get("/api/solicitudes")
                            .header(HttpHeaders.AUTHORIZATION, jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(solicitudId));

            mockMvc.perform(get("/api/solicitudes/{id}", solicitudId)
                            .header(HttpHeaders.AUTHORIZATION, jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(solicitudId))
                    .andExpect(jsonPath("$.estado").value("REGISTRADA"))
                    .andExpect(jsonPath("$.solicitanteId").value(solicitanteId));
        }
    }

    @Nested
    @DisplayName("Gestión de Usuarios")
    class GestionUsuarios {

        @Test
        void crearYListarUsuarios() throws Exception {
            String crearBody = """
                    {
                      "nombre": "Nuevo Estudiante",
                      "rol": "ESTUDIANTE",
                      "email": "nuevo@uniquindio.edu.co"
                    }
                    """;

            mockMvc.perform(post("/api/usuarios")
                            .header(HttpHeaders.AUTHORIZATION, jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crearBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Nuevo Estudiante"))
                    .andExpect(jsonPath("$.rol").value("ESTUDIANTE"))
                    .andExpect(jsonPath("$.email").value("nuevo@uniquindio.edu.co"));

            mockMvc.perform(get("/api/usuarios")
                            .header(HttpHeaders.AUTHORIZATION, jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].nombre").isString());
        }

        @Test
        void crearUsuarioSinAuth_devuelve403() throws Exception {
            mockMvc.perform(post("/api/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"nombre":"Test","rol":"ESTUDIANTE","email":"t@uniquindio.edu.co"}
                                    """))
                    .andExpect(status().isForbidden());
        }
    }
}
