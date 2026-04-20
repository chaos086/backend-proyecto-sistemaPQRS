package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de casos de uso de usuarios (Mockito).
 */
@ExtendWith(MockitoExtension.class)
class UsuarioUseCasesTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Test
    void crearUsuario_delegaGuardado() {
        CrearUsuarioUseCase useCase = new CrearUsuarioUseCase(usuarioRepository);
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = useCase.ejecutar("Nombre usuario", Rol.COORDINADOR, "mail@uniquindio.edu.co");

        assertEquals("Nombre usuario", resultado.nombre());
        assertEquals(Rol.COORDINADOR, resultado.rol());
        verify(usuarioRepository).guardar(any(Usuario.class));
    }

    @Test
    void obtenerUsuario_devuelveCuandoExiste() {
        ObtenerUsuarioUseCase useCase = new ObtenerUsuarioUseCase(usuarioRepository);
        UUID id = UUID.randomUUID();
        Usuario esperado = new Usuario(
                IdentificacionUsuario.of(id),
                "Ana",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                Email.of("ana@uniquindio.edu.co"));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(id))).thenReturn(Optional.of(esperado));

        assertEquals(esperado, useCase.ejecutar(id));
    }

    @Test
    void obtenerUsuario_lanzaCuandoNoExiste() {
        ObtenerUsuarioUseCase useCase = new ObtenerUsuarioUseCase(usuarioRepository);
        UUID id = UUID.randomUUID();
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(id))).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> useCase.ejecutar(id));
    }

    @Test
    void listarUsuarios_delegaEnRepositorio() {
        ListarUsuariosUseCase useCase = new ListarUsuariosUseCase(usuarioRepository);
        List<Usuario> lista = List.of();
        when(usuarioRepository.buscarTodas()).thenReturn(lista);

        assertEquals(lista, useCase.ejecutar());
        verify(usuarioRepository).buscarTodas();
    }

    @Test
    void activarUsuario_actualizaCuandoUsuarioExiste() {
        ActivarUsuarioUseCase useCase = new ActivarUsuarioUseCase(usuarioRepository);
        UUID id = UUID.randomUUID();
        Usuario inactivo = new Usuario(
                IdentificacionUsuario.of(id),
                "Inactivo",
                Rol.ADMINISTRATIVO,
                EstadoUsuario.INACTIVO,
                Email.of("adm@uniquindio.edu.co"));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(id))).thenReturn(Optional.of(inactivo));
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.ejecutar(id);

        verify(usuarioRepository).guardar(any(Usuario.class));
    }

    @Test
    void desactivarUsuario_actualizaCuandoUsuarioExiste() {
        DesactivarUsuarioUseCase useCase = new DesactivarUsuarioUseCase(usuarioRepository);
        UUID id = UUID.randomUUID();
        Usuario activo = new Usuario(
                IdentificacionUsuario.of(id),
                "Activo",
                Rol.PROFESOR,
                EstadoUsuario.ACTIVO,
                Email.of("prof@uniquindio.edu.co"));
        when(usuarioRepository.buscarPorId(IdentificacionUsuario.of(id))).thenReturn(Optional.of(activo));
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.ejecutar(id);

        verify(usuarioRepository).guardar(any(Usuario.class));
    }
}
