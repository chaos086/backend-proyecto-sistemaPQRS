package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueObject.Email;
import co.edu.uniquindio.proyecto.domain.valueObject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueObject.UsuarioReferencia;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.Rol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    /**
     * Verifica que un usuario se cree correctamente cuando se proporcionan
     * todos los datos requeridos.
     *
     * GIVEN: un nombre válido, un rol válido y un email válido
     * WHEN: se crea un usuario mediante el método crear()
     * THEN:
     *  - el usuario no es nulo
     *  - el email se asigna correctamente
     *  - el usuario queda activo por defecto
     */
    @Test
    void crearUsuarioValido() {
        Email email = new Email("usuario@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        assertNotNull(usuario);
        assertEquals(email, usuario.email());
        assertTrue(usuario.activo());
    }

    /**
     * Verifica que no se pueda crear un usuario con nombre nulo.
     *
     * GIVEN: un email válido y un rol válido
     * WHEN: se intenta crear un usuario con nombre null
     * THEN: se lanza una DomainException indicando que el nombre es inválido
     */
    @Test
    void crearUsuario_nombreNulo() {
        Email email = new Email("u@uniquindio.edu.co");
        assertThrows(DomainException.class, () -> Usuario.crear(null, Rol.ESTUDIANTE, email));
    }

    /**
     * Verifica que no se pueda crear un usuario con nombre en blanco.
     *
     * GIVEN: un email válido y un rol válido
     * WHEN: se intenta crear un usuario con un nombre compuesto solo por espacios
     * THEN: se lanza una DomainException indicando que el nombre es inválido
     */
    @Test
    void crearUsuario_nombreBlanco() {
        Email email = new Email("u@uniquindio.edu.co");
        assertThrows(DomainException.class, () -> Usuario.crear("   ", Rol.ESTUDIANTE, email));
    }

    /**
     * Verifica que no se pueda crear un usuario sin especificar un rol.
     *
     * GIVEN: un nombre válido y un email válido
     * WHEN: se intenta crear un usuario con rol null
     * THEN: se lanza una DomainException indicando que el rol es obligatorio
     */
    @Test
    void crearUsuario_rolNulo() {
        Email email = new Email("u@uniquindio.edu.co");
        assertThrows(DomainException.class, () -> Usuario.crear("Nombre", null, email));
    }

    /**
     * Verifica que un usuario pueda inicializarse como inactivo.
     *
     * GIVEN: un identificador válido, nombre, rol y email
     * WHEN: se crea un usuario con el estado activo en false
     * THEN: el usuario queda marcado como inactivo
     */
    @Test
    void crearUsuario_inactivo() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = new Usuario(IdentificacionUsuario.newId(), "Nombre", Rol.ESTUDIANTE, false, email);
        assertFalse(usuario.activo());
    }

    /**
     * Verifica que un usuario pueda ser desactivado correctamente.
     *
     * GIVEN: un usuario activo
     * WHEN: se invoca el método desactivar()
     * THEN: el usuario cambia su estado a inactivo
     */
    @Test
    void desactivarUsuario() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        usuario.desactivar();
        assertFalse(usuario.activo());
    }

    /**
     * Verifica que un usuario previamente desactivado pueda volver a activarse.
     *
     * GIVEN: un usuario desactivado
     * WHEN: se invoca el método activar()
     * THEN: el usuario cambia su estado a activo
     */
    @Test
    void activarUsuario() {
        Email email = new Email("u@uniquindio.edu.co");
        Usuario usuario = Usuario.crear("Nombre", Rol.ESTUDIANTE, email);
        usuario.desactivar();
        usuario.activar();
        assertTrue(usuario.activo());
    }

    /**
     * Verifica que un usuario pueda registrar una solicitud correctamente.
     *
     * GIVEN: un usuario válido y una referencia de usuario
     * WHEN: se agrega una solicitud registrada al usuario
     * THEN: la lista de solicitudes registradas contiene exactamente un elemento
     */
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
