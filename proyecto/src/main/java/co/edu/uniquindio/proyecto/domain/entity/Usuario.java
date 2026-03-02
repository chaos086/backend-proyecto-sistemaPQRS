package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueObject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueObject.UsuarioReferencia;
import co.edu.uniquindio.proyecto.domain.valueObject.enums.Rol;
import co.edu.uniquindio.proyecto.domain.valueObject.Email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad que representa un usuario del sistema de PQRS universitario.
 * Es la raíz del agregado de Usuario.
 * 
 * Un usuario puede ser:
 * - ESTUDIANTE: Puede crear solicitudes.
 * - DOCENTE: Puede ser asignado como responsable de solicitudes.
 * - COORDINADOR: Puede clasificar, priorizar y asignar responsables.
 * 
 * Sistema PQRS
 */
public class Usuario {

    private final IdentificacionUsuario id;
    private final String nombre;
    private final Rol rol;
    private final Email email;
    private boolean activo;

    private final List<UsuarioReferencia> solicitudesRegistradas;

    public Usuario(IdentificacionUsuario id, String nombre, Rol rol, boolean activo, Email email) {
        if (id == null) throw new DomainException("Usuario.id no puede ser null");
        if (nombre == null || nombre.isBlank()) throw new DomainException("Usuario.nombre es obligatorio");
        if (rol == null) throw new DomainException("Usuario.rol es obligatorio");
        if (email == null) throw new DomainException("Usuario.email es obligatorio");

        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
        this.email = email;
        this.solicitudesRegistradas = new ArrayList<>();
    }

    /**
     * Factory method para crear un nuevo usuario activo.
     * @param nombre Nombre completo del usuario
     * @param rol Rol del usuario en el sistema
     * @param email Correo electrónico del usuario
     * @return Nueva instancia de Usuario
     */
    public static Usuario crear(String nombre, Rol rol, Email email) {
        return new Usuario(IdentificacionUsuario.newId(), nombre, rol, true, email);
    }

    public IdentificacionUsuario id() { return id; }
    public String nombre() { return nombre; }
    public Rol rol() { return rol; }
    public Email email() { return email; }
    public boolean activo() { return activo; }

    /**
     * Desactiva el usuario (no puede crear ni atender solicitudes).
     */
    public void desactivar() { this.activo = false; }

    /**
     * Activa el usuario para participar en el sistema.
     */
    public void activar() { this.activo = true; }

    /**
     * Agrega una referencia a una solicitud registrada por este usuario.
     * Mantiene la trazabilidad de las solicitudes creadas.
     * @param solicitudRef Referencia a la solicitud
     */
    public void agregarSolicitudRegistrada(UsuarioReferencia solicitudRef) {
        if (solicitudRef == null) {
            throw new DomainException("No se puede agregar una referencia de solicitud null");
        }
        this.solicitudesRegistradas.add(solicitudRef);
    }

    public List<UsuarioReferencia> getSolicitudesRegistradas() {
        return Collections.unmodifiableList(solicitudesRegistradas);
    }

    public int cantidadSolicitudesRegistradas() {
        return solicitudesRegistradas.size();
    }
}
