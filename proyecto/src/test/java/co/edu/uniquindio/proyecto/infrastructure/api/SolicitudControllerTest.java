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
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.infrastructure.api.mapper.SolicitudRestMapperImpl;
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

@WebMvcTest(SolicitudController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, SolicitudRestMapperImpl.class})
class SolicitudControllerTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    CrearSolicitudUseCase crearSolicitudUseCase;
    @MockitoBean
    ClasificarSolicitudUseCase clasificarSolicitudUseCase;
    @MockitoBean
    PriorizarSolicitudUseCase priorizarSolicitudUseCase;
    @MockitoBean
    AsignarResponsableUseCase asignarResponsableUseCase;
    @MockitoBean
    CambiarEstadoUseCase cambiarEstadoUseCase;
    @MockitoBean
    CerrarSolicitudUseCase cerrarSolicitudUseCase;
    @MockitoBean
    ObtenerSolicitudUseCase obtenerSolicitudUseCase;
    @MockitoBean
    ListarSolicitudesUseCase listarSolicitudesUseCase;
    @MockitoBean
    ListarSolicitudesPorSolicitanteUseCase listarSolicitudesPorSolicitanteUseCase;
    @MockitoBean
    ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase;

    @MockitoBean
    JwtService jwtService;

    @Nested
    @DisplayName("POST /api/solicitudes")
    class CrearSolicitud {

        @TestFactory
        Stream<DynamicTest> cuerpoInvalido_rechazaCon400() {
            return Stream.of(
                    DynamicTest.dynamicTest("nombre muy corto",
                            () -> mockMvc.perform(post("/api/solicitudes")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(Map.of(
                                                    "solicitanteId", UUID.randomUUID().toString(),
                                                    "nombreSolicitante", "ab",
                                                    "canalOrigen", "PRESENCIAL",
                                                    "descripcion", "descripción válida larga"))))
                                    .andExpect(status().isBadRequest())),
                    DynamicTest.dynamicTest("descripción muy corta",
                            () -> mockMvc.perform(post("/api/solicitudes")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(Map.of(
                                                    "solicitanteId", UUID.randomUUID().toString(),
                                                    "nombreSolicitante", "Nombre válido",
                                                    "canalOrigen", "PRESENCIAL",
                                                    "descripcion", "corta"))))
                                    .andExpect(status().isBadRequest())),
                    DynamicTest.dynamicTest("solicitanteId nulo",
                            () -> mockMvc.perform(post("/api/solicitudes")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(Map.of(
                                                    "nombreSolicitante", "Nombre válido",
                                                    "canalOrigen", "PRESENCIAL",
                                                    "descripcion", "descripción válida larga"))))
                                    .andExpect(status().isBadRequest()))
            );
        }

        @Test
        void datosValidos_devuelve201() throws Exception {
            HistorialService historialService = new HistorialService();
            Solicitud creada = Solicitud.crear(
                    UUID.randomUUID(),
                    "Nombre del solicitante",
                    CanalOrigen.PRESENCIAL,
                    DescripcionSolicitud.of("Descripción con al menos diez caracteres válidos."),
                    historialService
            );
            when(crearSolicitudUseCase.ejecutar(any(), any(), any(), any())).thenReturn(creada);

            String body = """
                    {
                      "solicitanteId": "%s",
                      "nombreSolicitante": "Nombre del solicitante",
                      "canalOrigen": "PRESENCIAL",
                      "descripcion": "Descripción con al menos diez caracteres válidos."
                    }
                    """.formatted(UUID.randomUUID());

            mockMvc.perform(post("/api/solicitudes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.estado").value("REGISTRADA"));
        }
    }

    @Nested
    @DisplayName("GET /api/solicitudes/{id}")
    class ObtenerSolicitud {

        @Test
        void solicitudNoEncontrada_devuelve404() throws Exception {
            when(obtenerSolicitudUseCase.ejecutar(any(UUID.class)))
                    .thenThrow(new DomainException("Solicitud no encontrada"));

            mockMvc.perform(get("/api/solicitudes/{id}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Solicitud no encontrada"));
        }
    }
}
