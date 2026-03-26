package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.DomainException;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import co.edu.uniquindio.proyecto.domain.valueobject.IdentificacionUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;

public class Usuario {

    private final IdentificacionUsuario id;
    private final String nombre;
    private final Rol rol;
    private final Email email;
    private EstadoUsuario estado;

    public Usuario(IdentificacionUsuario id, String nombre, Rol rol, EstadoUsuario estado, Email email) {
        if (id == null) throw new DomainException("Usuario.id no puede ser null");
        if (nombre == null || nombre.isBlank()) throw new DomainException("Usuario.nombre es obligatorio");
        if (rol == null) throw new DomainException("Usuario.rol es obligatorio");
        if (email == null) throw new DomainException("Usuario.email es obligatorio");
        if (estado == null) throw new DomainException("Usuario.estado es obligatorio");

        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.estado = estado;
        this.email = email;
    }

    public static Usuario crear(String nombre, Rol rol, Email email) {
        return new Usuario(IdentificacionUsuario.newId(), nombre, rol, EstadoUsuario.ACTIVO, email);
    }

    public IdentificacionUsuario id() { return id; }
    public String nombre() { return nombre; }
    public Rol rol() { return rol; }
    public Email email() { return email; }
    public EstadoUsuario estado() { return estado; }

    public boolean activo() {
        return estado == EstadoUsuario.ACTIVO;
    }

    public void desactivar() {
        this.estado = EstadoUsuario.INACTIVO;
    }

    public void activar() {
        this.estado = EstadoUsuario.ACTIVO;
    }
}
