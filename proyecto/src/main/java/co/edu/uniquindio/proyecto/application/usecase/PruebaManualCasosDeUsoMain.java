package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.CanalOrigen;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.TipoSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.persistence.memory.InMemorySolicitudRepository;
import co.edu.uniquindio.proyecto.infrastructure.persistence.memory.InMemoryUsuarioRepository;

/**
 * Clase auxiliar para probar manualmente los casos de uso de la guía 06
 * utilizando repositorios en memoria.
 */
public class PruebaManualCasosDeUsoMain {

    public static void main(String[] args) {
        InMemorySolicitudRepository solicitudRepository = new InMemorySolicitudRepository();
        InMemoryUsuarioRepository usuarioRepository = new InMemoryUsuarioRepository();
        SolicitudDomainService solicitudDomainService = new SolicitudDomainService();
        HistorialService historialService = new HistorialService();

        CrearSolicitudUseCase crearSolicitudUseCase = new CrearSolicitudUseCase(
                solicitudRepository,
                usuarioRepository,
                solicitudDomainService,
                historialService
        );

        AsignarResponsableUseCase asignarResponsableUseCase = new AsignarResponsableUseCase(
                solicitudRepository,
                usuarioRepository,
                solicitudDomainService
        );

        CambiarEstadoUseCase cambiarEstadoUseCase = new CambiarEstadoUseCase(
                solicitudRepository,
                usuarioRepository,
                solicitudDomainService
        );

        CerrarSolicitudUseCase cerrarSolicitudUseCase = new CerrarSolicitudUseCase(
                solicitudRepository,
                usuarioRepository,
                solicitudDomainService
        );

        ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase =
                new ConsultarSolicitudesPorEstadoUseCase(solicitudRepository);

        Usuario solicitante = Usuario.crear("Ana Estudiante", Rol.ESTUDIANTE, new Email("ana@uq.edu.co"));
        Usuario coordinador = Usuario.crear("Carlos Coordinador", Rol.COORDINADOR, new Email("carlos@uq.edu.co"));
        Usuario profesor = Usuario.crear("Laura Profesora", Rol.PROFESOR, new Email("laura@uq.edu.co"));

        usuarioRepository.guardar(solicitante);
        usuarioRepository.guardar(coordinador);
        usuarioRepository.guardar(profesor);

        Solicitud solicitud = crearSolicitudUseCase.ejecutar(
                java.util.UUID.fromString(solicitante.id().valor()),
                solicitante.nombre(),
                CanalOrigen.APLICACION_WEB,
                "Necesito apoyo con una solicitud academica urgente"
        );

        solicitud.clasificar(
                TipoSolicitud.PETICION,
                java.util.UUID.fromString(coordinador.id().valor()),
                coordinador.nombre()
        );
        solicitudRepository.guardar(solicitud);

        asignarResponsableUseCase.ejecutar(
                java.util.UUID.fromString(solicitud.id().valor()),
                java.util.UUID.fromString(profesor.id().valor()),
                java.util.UUID.fromString(coordinador.id().valor())
        );

        cambiarEstadoUseCase.ejecutar(
                java.util.UUID.fromString(solicitud.id().valor()),
                java.util.UUID.fromString(profesor.id().valor()),
                "Solicitud atendida correctamente"
        );

        cerrarSolicitudUseCase.ejecutar(
                java.util.UUID.fromString(solicitud.id().valor()),
                java.util.UUID.fromString(profesor.id().valor()),
                "Caso cerrado satisfactoriamente"
        );

        System.out.println("Solicitudes cerradas: "
                + consultarSolicitudesPorEstadoUseCase.ejecutar(EstadoSolicitud.CERRADA).size());
    }
}
