package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.pqrs;

import co.edu.uniquindio.proyecto.domain.valueobject.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.enums.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA para usuarios del modelo PQRS (persistencia relacional).
 */
@Entity
@Table(name = "pqrs_usuarios")
public class UsuarioEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private EstadoUsuario estado;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    protected UsuarioEntity() {}

    public UsuarioEntity(String id, String nombre, Rol rol, EstadoUsuario estado, String email) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.estado = estado;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public String getEmail() {
        return email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
