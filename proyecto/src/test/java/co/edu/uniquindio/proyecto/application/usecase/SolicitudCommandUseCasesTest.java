package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Prioridad;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de casos de uso de solicitudes que modifican estado o persisten (Mockito).
 */
@ExtendWith(MockitoExtension.class)
class SolicitudCommandUseCasesTest {

    @Mock
    SolicitudRepository solicitudRepository;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    SolicitudDomainService solicitudDomainService;
    @Mock
    HistorialService historialService;

    @Test
    void crearSolicitud_guardaCuandoSolicitanteExisteYDominioAprueba() {
        CrearSolicitudUseCase useCase = new CrearSolicitudUseCase(
                solicitudRepository, usuarioRepository, solicitudDomainService, historialService);

        UUID solicitanteId = UUID.randomUUID();
        Usuario estudiante = Usuario.crear("María Estudiante", Rol.ESTUDIANTE, Email.of("maria@uniquindio.edu.co"));
        when(usuarioRepository.buscarPorId(any(IdentificacionUsuario.class))).thenReturn(Optional.of(estudiante));
        when(solicitudRepository.buscarTodas()).thenReturn(List.of());
        when(solicitudRepository.guardar(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitud resultado = useCase.ejecutar(
                solicitanteId,
                "María Estudiante",
                CanalOrigen.PRESENCIAL,
                "Descripción con más de diez caracteres válidos.");

        assertEquals(solicitanteId, resultado.solicitanteId());
        verify(solicitudDomainService).validarCrearSolicitud(eq(estudiante), anyList());
        verify(solicitudRepository).guardar(any(Solicitud.class));
    }

    @Test
    void clasificarSolicitud_propagaCuandoNoExisteLaSolicitud() {
        ClasificarSolicitudUseCase useCase = new ClasificarSolicitudUseCase(
                solicitudRepository, usuarioRepository, solicitudDomainService);

        UUID id = UUID.randomUUID();
        when(solicitudRepository.buscarPorId(SolicitudId.of(id))).thenReturn(Optional.empty());

        assertThrows(DomainException.class,
                () -> useCase.ejecutar(id, TipoSolicitud.PETICION, UUID.randomUUID()));
    }

    @Test
    void clasificarSolicitud_actualizaCuandoHayCoordinador() {
        ClasificarSolicitudUseCase useCase = new ClasificarSolicitudUseCase(
                solicitudRepository, usuarioRepository, solicitudDomainService);

        UUID solicitudUuid = UUID.randomUUID();
        UUID solicitanteUuid = UUID.randomUUID();
        UUID coordUuid = UUID.randomUUID();
        Solicitud enRegistro = SolicitudTestFactory.solicitudRegistrada(solicitudUuid, solicitanteUuid);
        Usuario coordinador = new Usuario(
                IdentificacionUsuario.of(coordUuid),
                "Coord",
                Rol.COORDINADOR,
                EstadoUsuario.ACTIVO,
                Email.of("coord@uniquindio.edu.co"));

        when(solicitudRepository.buscarPorId(SolicitudId.of(solicitudUuid))).thenReturn(Optional.of(enRegistro));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(coordUuid))).thenReturn(Optional.of(coordinador));
        when(solicitudRepository.guardar(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitud resultado = useCase.ejecutar(solicitudUuid, TipoSolicitud.QUEJA, coordUuid);

        assertEquals(TipoSolicitud.QUEJA, resultado.tipoSolicitud());
        verify(solicitudDomainService).validarClasificar(any(Solicitud.class));
        verify(solicitudRepository).guardar(any(Solicitud.class));
    }

    @Test
    void priorizarSolicitud_guardaCuandoDominioAprueba() {
        PriorizarSolicitudUseCase useCase = new PriorizarSolicitudUseCase(
                solicitudRepository, usuarioRepository, solicitudDomainService);

        UUID solicitudUuid = UUID.randomUUID();
        UUID solicitanteUuid = UUID.randomUUID();
        UUID coordUuid = UUID.randomUUID();
        Solicitud clasificada = SolicitudTestFactory.solicitudClasificada(solicitudUuid, solicitanteUuid, coordUuid);
        Usuario coordinador = new Usuario(
                IdentificacionUsuario.of(coordUuid),
                "Coord",
                Rol.COORDINADOR,
                EstadoUsuario.ACTIVO,
                Email.of("coord@uniquindio.edu.co"));

        when(solicitudRepository.buscarPorId(SolicitudId.of(solicitudUuid))).thenReturn(Optional.of(clasificada));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(coordUuid))).thenReturn(Optional.of(coordinador));
        when(solicitudRepository.guardar(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitud resultado = useCase.ejecutar(
                solicitudUuid,
                Prioridad.ALTA,
                "Justificación de prioridad con más de cinco caracteres.",
                coordUuid);

        assertEquals(Prioridad.ALTA, resultado.prioridad());
        verify(solicitudDomainService).validarPriorizar(any(Solicitud.class));
        verify(solicitudRepository).guardar(any(Solicitud.class));
    }

    @Test
    void asignarResponsable_guardaCuandoProfesorEsValido() {
        AsignarResponsableUseCase useCase = new AsignarResponsableUseCase(
                solicitudRepository, usuarioRepository, solicitudDomainService);

        UUID solicitudUuid = UUID.randomUUID();
        UUID solicitanteUuid = UUID.randomUUID();
        UUID coordUuid = UUID.randomUUID();
        UUID profUuid = UUID.randomUUID();

        Solicitud clasificada = SolicitudTestFactory.solicitudClasificada(solicitudUuid, solicitanteUuid, coordUuid);
        Usuario profesor = new Usuario(
                IdentificacionUsuario.of(profUuid),
                "Prof",
                Rol.PROFESOR,
                EstadoUsuario.ACTIVO,
                Email.of("prof@uniquindio.edu.co"));
        Usuario coordinador = new Usuario(
                IdentificacionUsuario.of(coordUuid),
                "Coord",
                Rol.COORDINADOR,
                EstadoUsuario.ACTIVO,
                Email.of("coord@uniquindio.edu.co"));

        when(solicitudRepository.buscarPorId(SolicitudId.of(solicitudUuid))).thenReturn(Optional.of(clasificada));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(profUuid))).thenReturn(Optional.of(profesor));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(coordUuid))).thenReturn(Optional.of(coordinador));
        when(solicitudRepository.buscarTodas()).thenReturn(List.of());
        when(solicitudRepository.guardar(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitud resultado = useCase.ejecutar(solicitudUuid, profUuid, coordUuid);

        assertEquals(profUuid, resultado.responsableId());
        verify(solicitudDomainService).validarAsignarResponsable(any(Solicitud.class), eq(profesor), anyList());
        verify(solicitudRepository).guardar(any(Solicitud.class));
    }

    @Test
    void cambiarEstado_marcaAtendidaCuandoValidacionesPasen() {
        CambiarEstadoUseCase useCase = new CambiarEstadoUseCase(
                solicitudRepository, usuarioRepository, solicitudDomainService);

        UUID solicitudUuid = UUID.randomUUID();
        UUID solicitanteUuid = UUID.randomUUID();
        UUID coordUuid = UUID.randomUUID();
        UUID profUuid = UUID.randomUUID();

        Solicitud enAtencion = SolicitudTestFactory.solicitudEnAtencion(
                solicitudUuid, solicitanteUuid, coordUuid, profUuid);
        Usuario profesor = new Usuario(
                IdentificacionUsuario.of(profUuid),
                "Prof",
                Rol.PROFESOR,
                EstadoUsuario.ACTIVO,
                Email.of("prof@uniquindio.edu.co"));

        when(solicitudRepository.buscarPorId(SolicitudId.of(solicitudUuid))).thenReturn(Optional.of(enAtencion));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(profUuid))).thenReturn(Optional.of(profesor));
        when(solicitudRepository.guardar(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitud resultado = useCase.ejecutar(solicitudUuid, profUuid, "Observación del profesor sobre la gestión.");

        assertNotNull(resultado);
        verify(solicitudDomainService).validarMarcarAtendida(any(Solicitud.class), eq(profUuid));
        verify(solicitudRepository).guardar(any(Solicitud.class));
    }

    @Test
    void cerrarSolicitud_guardaCuandoEstabaAtendida() {
        CerrarSolicitudUseCase useCase = new CerrarSolicitudUseCase(
                solicitudRepository, usuarioRepository, solicitudDomainService);

        UUID solicitudUuid = UUID.randomUUID();
        UUID solicitanteUuid = UUID.randomUUID();
        UUID coordUuid = UUID.randomUUID();
        UUID profUuid = UUID.randomUUID();

        Solicitud atendida = SolicitudTestFactory.solicitudAtendida(
                solicitudUuid, solicitanteUuid, coordUuid, profUuid);
        Usuario profesor = new Usuario(
                IdentificacionUsuario.of(profUuid),
                "Prof",
                Rol.PROFESOR,
                EstadoUsuario.ACTIVO,
                Email.of("prof@uniquindio.edu.co"));

        when(solicitudRepository.buscarPorId(SolicitudId.of(solicitudUuid))).thenReturn(Optional.of(atendida));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(profUuid))).thenReturn(Optional.of(profesor));
        when(solicitudRepository.guardar(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitud resultado = useCase.ejecutar(solicitudUuid, profUuid, "Cierre formal con observación obligatoria.");

        assertNotNull(resultado);
        verify(solicitudDomainService).validarCerrar(any(Solicitud.class));
        verify(solicitudRepository).guardar(any(Solicitud.class));
    }
}
