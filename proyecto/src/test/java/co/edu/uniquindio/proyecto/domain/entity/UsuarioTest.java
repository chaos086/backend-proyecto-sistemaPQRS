package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueObject.Email;
import co.edu.uniquindio.proyecto.domain.valueObject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueObject.UsuarioReferencia;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.Rol;
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
    void crearUsuario_inactivo() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = new Usuario(IdentificacionUsuario.newId(), "Nombre", Rol.ESTUDIANTE, false, email);
        assertFalse(usuario.activo());
    }

    @Test
    void desactivarUsuario() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        usuario.desactivar();
        assertFalse(usuario.activo());
    }

    @Test
    void activarUsuario() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        usuario.desactivar();
        usuario.activar();
        assertTrue(usuario.activo());
    }

    @Test
    void agregarSolicitudRegistradaYVerificar() {
        Email email = new Email("u2@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        IdentificacionUsuario id = IdentificacionUsuario.newId();
        UsuarioReferencia ref = new UsuarioReferencia(id.value(), "Solicitante");
        usuario.agregarSolicitudRegistrada(ref);
        assertEquals(1, usuario.getSolicitudesRegistradas().size());
    }
}
