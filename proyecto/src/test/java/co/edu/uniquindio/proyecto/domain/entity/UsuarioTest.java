package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void crearUsuarioValido() {
        Email email = new Email("usuario@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        assertNotNull(usuario);
        assertEquals(email, usuario.email());
        assertTrue(usuario.activo());
        assertEquals(EstadoUsuario.ACTIVO, usuario.estado());
    }

    @Test
    void crearUsuario_nombreNulo() {
        Email email = new Email("u@uniquindio.edu.co");
        assertThrows(DomainException.class, () -> Usuario.crear(null, Rol.ESTUDIANTE, email));
    }

    @Test
    void crearUsuario_nombreBlanco() {
        Email email = new Email("u@uniquindio.edu.co");
        assertThrows(DomainException.class, () -> Usuario.crear("   ", Rol.ESTUDIANTE, email));
    }

    @Test
    void crearUsuario_rolNulo() {
        Email email = new Email("u@uniquindio.edu.co");
        assertThrows(DomainException.class, () -> Usuario.crear("Nombre", null, email));
    }

    @Test
    void crearUsuario_estadoInactivo() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = new Usuario(IdentificacionUsuario.newId(), "Nombre", Rol.ESTUDIANTE, EstadoUsuario.INACTIVO, email);
        assertFalse(usuario.activo());
        assertEquals(EstadoUsuario.INACTIVO, usuario.estado());
    }

    @Test
    void desactivarUsuario() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        usuario.desactivar();
        assertFalse(usuario.activo());
        assertEquals(EstadoUsuario.INACTIVO, usuario.estado());
    }

    @Test
    void activarUsuario() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        usuario.desactivar();
        usuario.activar();
        assertTrue(usuario.activo());
        assertEquals(EstadoUsuario.ACTIVO, usuario.estado());
    }
}
