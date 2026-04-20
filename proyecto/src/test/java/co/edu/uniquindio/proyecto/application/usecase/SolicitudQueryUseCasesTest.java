package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de casos de uso de consulta de solicitudes (Mockito).
 */
@ExtendWith(MockitoExtension.class)
class SolicitudQueryUseCasesTest {

    @Mock
    SolicitudRepository solicitudRepository;

    @Test
    void obtenerSolicitud_devuelveCuandoExiste() {
        ObtenerSolicitudUseCase useCase = new ObtenerSolicitudUseCase(solicitudRepository);
        UUID id = UUID.randomUUID();
        Solicitud esperada = SolicitudTestFactory.solicitudRegistrada(id, UUID.randomUUID());
        when(solicitudRepository.buscarPorId(SolicitudId.of(id))).thenReturn(Optional.of(esperada));

        Solicitud resultado = useCase.ejecutar(id);

        assertEquals(esperada, resultado);
        verify(solicitudRepository).buscarPorId(SolicitudId.of(id));
    }

    @Test
    void obtenerSolicitud_lanzaCuandoNoExiste() {
        ObtenerSolicitudUseCase useCase = new ObtenerSolicitudUseCase(solicitudRepository);
        UUID id = UUID.randomUUID();
        when(solicitudRepository.buscarPorId(SolicitudId.of(id))).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> useCase.ejecutar(id));
    }

    @Test
    void listarSolicitudes_delegaEnRepositorio() {
        ListarSolicitudesUseCase useCase = new ListarSolicitudesUseCase(solicitudRepository);
        List<Solicitud> lista = List.of();
        when(solicitudRepository.buscarTodas()).thenReturn(lista);

        assertEquals(lista, useCase.ejecutar());
        verify(solicitudRepository).buscarTodas();
    }

    @Test
    void listarPorSolicitante_delegaEnRepositorio() {
        ListarSolicitudesPorSolicitanteUseCase useCase =
                new ListarSolicitudesPorSolicitanteUseCase(solicitudRepository);
        UUID sid = UUID.randomUUID();
        List<Solicitud> lista = List.of();
        when(solicitudRepository.buscarPorSolicitanteId(sid)).thenReturn(lista);

        assertEquals(lista, useCase.ejecutar(sid));
        verify(solicitudRepository).buscarPorSolicitanteId(sid);
    }

    @Test
    void consultarPorEstado_delegaEnRepositorio() {
        ConsultarSolicitudesPorEstadoUseCase useCase =
                new ConsultarSolicitudesPorEstadoUseCase(solicitudRepository);
        EstadoSolicitud estado = EstadoSolicitud.REGISTRADA;
        List<Solicitud> lista = List.of();
        when(solicitudRepository.buscarPorEstado(estado)).thenReturn(lista);

        assertEquals(lista, useCase.ejecutar(estado));
        verify(solicitudRepository).buscarPorEstado(estado);
    }
}
