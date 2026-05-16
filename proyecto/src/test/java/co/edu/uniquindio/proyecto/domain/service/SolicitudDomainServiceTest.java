package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.SolicitudId;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolicitudDomainServiceTest {

    private final SolicitudDomainService service = new SolicitudDomainService();
    private final HistorialService historialService = new HistorialService();

    private Usuario usuarioActivo(UUID id, Rol rol) {
        return new Usuario(
                IdentificacionUsuario.of(id),
                "Nombre",
                rol,
                EstadoUsuario.ACTIVO,
                Email.of("test@uniquindio.edu.co"));
    }

    private Usuario usuarioInactivo(UUID id, Rol rol) {
        return new Usuario(
                IdentificacionUsuario.of(id),
                "Nombre",
                rol,
                EstadoUsuario.INACTIVO,
                Email.of("test@uniquindio.edu.co"));
    }

    private Solicitud solicitudEnEstado(UUID solicitudId, UUID solicitanteId, EstadoSolicitud estado, UUID responsableId) {
        return Solicitud.rehidratar(
                SolicitudId.of(solicitudId),
                solicitanteId,
                "Solicitante",
                co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen.PRESENCIAL,
                Instant.now(),
                co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud.PETICION,
                co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud.of("Descripción con mínimo diez caracteres."),
                null,
                null,
                estado,
                responsableId,
                responsableId != null ? "Responsable" : null,
                List.of(),
                historialService);
    }

    private Solicitud solicitudEnEstado(UUID solicitudId, UUID solicitanteId, EstadoSolicitud estado) {
        return solicitudEnEstado(solicitudId, solicitanteId, estado, null);
    }

    // =========================================================================
    // validarCrearSolicitud
    // =========================================================================

    @Nested
    @DisplayName("validarCrearSolicitud")
    class ValidarCrearSolicitud {

        @Test
        void nullSolicitante_lanzaExcepcion() {
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarCrearSolicitud(null, List.of()));
            assertEquals("El solicitante no puede ser null", ex.getMessage());
        }

        @Test
        void solicitanteInactivo_lanzaExcepcion() {
            Usuario inactivo = usuarioInactivo(UUID.randomUUID(), Rol.ESTUDIANTE);
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarCrearSolicitud(inactivo, List.of()));
            assertEquals("El solicitante debe estar activo", ex.getMessage());
        }

        @TestFactory
        Stream<DynamicTest> solicitanteConDemasiadasPendientes_lanzaExcepcion() {
            UUID solicitanteId = UUID.randomUUID();
            List<Solicitud> pendientes = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                pendientes.add(solicitudEnEstado(
                        UUID.randomUUID(), solicitanteId, EstadoSolicitud.REGISTRADA));
            }

            Usuario activo = usuarioActivo(solicitanteId, Rol.ESTUDIANTE);

            return Stream.of(
                    DynamicTest.dynamicTest("5 REGISTRADAS -> error",
                            () -> {
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarCrearSolicitud(activo, pendientes));
                                assertEquals("Un solicitante no puede tener más de 5 solicitudes pendientes",
                                        ex.getMessage());
                            }),
                    DynamicTest.dynamicTest("5 CLASIFICADAS -> error",
                            () -> {
                                List<Solicitud> clasificadas = pendientes.stream()
                                        .map(s -> solicitudEnEstado(
                                                UUID.randomUUID(), solicitanteId, EstadoSolicitud.CLASIFICADA))
                                        .toList();
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarCrearSolicitud(activo, clasificadas));
                                assertEquals("Un solicitante no puede tener más de 5 solicitudes pendientes",
                                        ex.getMessage());
                            }),
                    DynamicTest.dynamicTest("3 EN_ATENCION + 2 REGISTRADAS -> error",
                            () -> {
                                List<Solicitud> mixtas = new ArrayList<>();
                                for (int i = 0; i < 3; i++)
                                    mixtas.add(solicitudEnEstado(
                                            UUID.randomUUID(), solicitanteId, EstadoSolicitud.EN_ATENCION));
                                for (int i = 0; i < 2; i++)
                                    mixtas.add(solicitudEnEstado(
                                            UUID.randomUUID(), solicitanteId, EstadoSolicitud.REGISTRADA));
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarCrearSolicitud(activo, mixtas));
                                assertEquals("Un solicitante no puede tener más de 5 solicitudes pendientes",
                                        ex.getMessage());
                            })
            );
        }

        @Test
        void datosValidos_noLanzaExcepcion() {
            UUID id = UUID.randomUUID();
            Usuario activo = usuarioActivo(id, Rol.ESTUDIANTE);
            assertDoesNotThrow(() -> service.validarCrearSolicitud(activo, List.of()));
        }

        @Test
        void menosDe5Pendientes_noLanzaExcepcion() {
            UUID solicitanteId = UUID.randomUUID();
            Usuario activo = usuarioActivo(solicitanteId, Rol.ESTUDIANTE);
            List<Solicitud> pendientes = List.of(
                    solicitudEnEstado(UUID.randomUUID(), solicitanteId, EstadoSolicitud.REGISTRADA));
            assertDoesNotThrow(() -> service.validarCrearSolicitud(activo, pendientes));
        }

        @Test
        void solicitudesDeOtroSolicitanteNoCuentan() {
            UUID solicitanteId = UUID.randomUUID();
            Usuario activo = usuarioActivo(solicitanteId, Rol.ESTUDIANTE);
            List<Solicitud> deOtro = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                deOtro.add(solicitudEnEstado(
                        UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.REGISTRADA));
            }
            assertDoesNotThrow(() -> service.validarCrearSolicitud(activo, deOtro));
        }
    }

    // =========================================================================
    // validarClasificar
    // =========================================================================

    @Nested
    @DisplayName("validarClasificar")
    class ValidarClasificar {

        @Test
        void nullSolicitud_lanzaExcepcion() {
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarClasificar(null));
            assertEquals("La solicitud no puede ser null", ex.getMessage());
        }

        @TestFactory
        Stream<DynamicTest> estadoIncorrecto_lanzaExcepcion() {
            UUID sid = UUID.randomUUID();
            UUID solId = UUID.randomUUID();
            return Stream.of(EstadoSolicitud.CLASIFICADA, EstadoSolicitud.EN_ATENCION,
                            EstadoSolicitud.ATENDIDA, EstadoSolicitud.CERRADA)
                    .map(est -> DynamicTest.dynamicTest("estado " + est + " -> error",
                            () -> {
                                Solicitud s = solicitudEnEstado(sid, solId, est);
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarClasificar(s));
                                assertEquals("Solo se puede clasificar una solicitud en estado REGISTRADA",
                                        ex.getMessage());
                            }));
        }

        @Test
        void registrada_noLanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.REGISTRADA);
            assertDoesNotThrow(() -> service.validarClasificar(s));
        }
    }

    // =========================================================================
    // validarPriorizar
    // =========================================================================

    @Nested
    @DisplayName("validarPriorizar")
    class ValidarPriorizar {

        @Test
        void nullSolicitud_lanzaExcepcion() {
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarPriorizar(null));
            assertEquals("La solicitud no puede ser null", ex.getMessage());
        }

        @TestFactory
        Stream<DynamicTest> estadoIncorrecto_lanzaExcepcion() {
            UUID sid = UUID.randomUUID();
            UUID solId = UUID.randomUUID();
            return Stream.of(EstadoSolicitud.REGISTRADA, EstadoSolicitud.EN_ATENCION,
                            EstadoSolicitud.ATENDIDA, EstadoSolicitud.CERRADA)
                    .map(est -> DynamicTest.dynamicTest("estado " + est + " -> error",
                            () -> {
                                Solicitud s = solicitudEnEstado(sid, solId, est);
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarPriorizar(s));
                                assertEquals("Solo se puede priorizar una solicitud en estado CLASIFICADA",
                                        ex.getMessage());
                            }));
        }

        @Test
        void clasificada_noLanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.CLASIFICADA);
            assertDoesNotThrow(() -> service.validarPriorizar(s));
        }
    }

    // =========================================================================
    // validarAsignarResponsable
    // =========================================================================

    @Nested
    @DisplayName("validarAsignarResponsable")
    class ValidarAsignarResponsable {

        @Test
        void nullSolicitud_lanzaExcepcion() {
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarAsignarResponsable(null, usuarioActivo(UUID.randomUUID(), Rol.PROFESOR), List.of()));
            assertEquals("La solicitud no puede ser null", ex.getMessage());
        }

        @Test
        void nullResponsable_lanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.CLASIFICADA);
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarAsignarResponsable(s, null, List.of()));
            assertEquals("El responsable no puede ser null", ex.getMessage());
        }

        @TestFactory
        Stream<DynamicTest> estadoIncorrecto_lanzaExcepcion() {
            UUID sid = UUID.randomUUID();
            UUID solId = UUID.randomUUID();
            Usuario profe = usuarioActivo(UUID.randomUUID(), Rol.PROFESOR);
            return Stream.of(EstadoSolicitud.REGISTRADA, EstadoSolicitud.EN_ATENCION,
                            EstadoSolicitud.ATENDIDA, EstadoSolicitud.CERRADA)
                    .map(est -> DynamicTest.dynamicTest("estado " + est + " -> error",
                            () -> {
                                Solicitud s = solicitudEnEstado(sid, solId, est);
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarAsignarResponsable(s, profe, List.of()));
                                assertEquals("Solo se puede asignar responsable en estado CLASIFICADA",
                                        ex.getMessage());
                            }));
        }

        @Test
        void responsableInactivo_lanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.CLASIFICADA);
            Usuario inactivo = usuarioInactivo(UUID.randomUUID(), Rol.PROFESOR);
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarAsignarResponsable(s, inactivo, List.of()));
            assertEquals("El responsable debe estar activo", ex.getMessage());
        }

        @Test
        void responsableNoProfesor_lanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.CLASIFICADA);
            Usuario estudiante = usuarioActivo(UUID.randomUUID(), Rol.ESTUDIANTE);
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarAsignarResponsable(s, estudiante, List.of()));
            assertEquals("Solo un profesor puede ser asignado como responsable", ex.getMessage());
        }

        @Test
        void profesorCon10SolicitudesEnAtencion_lanzaExcepcion() {
            UUID profId = UUID.randomUUID();
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.CLASIFICADA);
            Usuario profe = usuarioActivo(profId, Rol.PROFESOR);
            List<Solicitud> enAtencion = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                enAtencion.add(solicitudEnEstado(
                        UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.EN_ATENCION, profId));
            }
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarAsignarResponsable(s, profe, enAtencion));
            assertEquals("Un profesor no puede tener más de 10 solicitudes en atención",
                    ex.getMessage());
        }

        @Test
        void datosValidos_noLanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.CLASIFICADA);
            Usuario profe = usuarioActivo(UUID.randomUUID(), Rol.PROFESOR);
            assertDoesNotThrow(() -> service.validarAsignarResponsable(s, profe, List.of()));
        }

        @Test
        void solicitudesDeOtroProfesorNoCuentan() {
            UUID profId = UUID.randomUUID();
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.CLASIFICADA);
            Usuario profe = usuarioActivo(profId, Rol.PROFESOR);
            List<Solicitud> deOtroProfe = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                deOtroProfe.add(solicitudEnEstado(
                        UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.EN_ATENCION, UUID.randomUUID()));
            }
            assertDoesNotThrow(() -> service.validarAsignarResponsable(s, profe, deOtroProfe));
        }
    }

    // =========================================================================
    // validarMarcarAtendida
    // =========================================================================

    @Nested
    @DisplayName("validarMarcarAtendida")
    class ValidarMarcarAtendida {

        @Test
        void nullSolicitud_lanzaExcepcion() {
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarMarcarAtendida(null, UUID.randomUUID()));
            assertEquals("La solicitud no puede ser null", ex.getMessage());
        }

        @Test
        void nullResponsableId_lanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.EN_ATENCION);
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarMarcarAtendida(s, null));
            assertEquals("El responsable no puede ser null", ex.getMessage());
        }

        @TestFactory
        Stream<DynamicTest> estadoIncorrecto_lanzaExcepcion() {
            UUID sid = UUID.randomUUID();
            UUID solId = UUID.randomUUID();
            UUID respId = UUID.randomUUID();
            return Stream.of(EstadoSolicitud.REGISTRADA, EstadoSolicitud.CLASIFICADA,
                            EstadoSolicitud.ATENDIDA, EstadoSolicitud.CERRADA)
                    .map(est -> DynamicTest.dynamicTest("estado " + est + " -> error",
                            () -> {
                                Solicitud s = solicitudEnEstado(sid, solId, est, respId);
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarMarcarAtendida(s, respId));
                                assertEquals("Solo se puede atender una solicitud en estado EN_ATENCION",
                                        ex.getMessage());
                            }));
        }

        @Test
        void sinResponsableAsignado_lanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.EN_ATENCION, null);
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarMarcarAtendida(s, UUID.randomUUID()));
            assertEquals("No se puede atender sin responsable asignado", ex.getMessage());
        }

        @Test
        void responsableIdDiferente_lanzaExcepcion() {
            UUID respActual = UUID.randomUUID();
            UUID respDiferente = UUID.randomUUID();
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.EN_ATENCION, respActual);
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarMarcarAtendida(s, respDiferente));
            assertEquals("Solo el responsable asignado puede marcar como atendida", ex.getMessage());
        }

        @Test
        void datosValidos_noLanzaExcepcion() {
            UUID respId = UUID.randomUUID();
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.EN_ATENCION, respId);
            assertDoesNotThrow(() -> service.validarMarcarAtendida(s, respId));
        }
    }

    // =========================================================================
    // validarCerrar
    // =========================================================================

    @Nested
    @DisplayName("validarCerrar")
    class ValidarCerrar {

        @Test
        void nullSolicitud_lanzaExcepcion() {
            DomainException ex = assertThrows(DomainException.class,
                    () -> service.validarCerrar(null));
            assertEquals("La solicitud no puede ser null", ex.getMessage());
        }

        @TestFactory
        Stream<DynamicTest> estadoIncorrecto_lanzaExcepcion() {
            UUID sid = UUID.randomUUID();
            UUID solId = UUID.randomUUID();
            return Stream.of(EstadoSolicitud.REGISTRADA, EstadoSolicitud.CLASIFICADA,
                            EstadoSolicitud.EN_ATENCION, EstadoSolicitud.CERRADA)
                    .map(est -> DynamicTest.dynamicTest("estado " + est + " -> error",
                            () -> {
                                Solicitud s = solicitudEnEstado(sid, solId, est);
                                DomainException ex = assertThrows(DomainException.class,
                                        () -> service.validarCerrar(s));
                                assertEquals("Solo se puede cerrar una solicitud que haya sido ATENDIDA",
                                        ex.getMessage());
                            }));
        }

        @Test
        void atendida_noLanzaExcepcion() {
            Solicitud s = solicitudEnEstado(UUID.randomUUID(), UUID.randomUUID(), EstadoSolicitud.ATENDIDA);
            assertDoesNotThrow(() -> service.validarCerrar(s));
        }
    }
}
