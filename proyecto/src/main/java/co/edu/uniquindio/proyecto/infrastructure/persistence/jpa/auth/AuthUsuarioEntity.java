package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.auth;

import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.embeddable.EmailCorporativo;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Credenciales persistidas para autenticación (capa infraestructura / BD).
 * El correo es un VO embebido ({@link EmailCorporativo}) mapeado a la columna {@code email}.
 */
@Entity
@Table(name = "auth_usuarios")
public class AuthUsuarioEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "valor",
                    column = @Column(name = "email", nullable = false, unique = true, length = 255))
    })
    private EmailCorporativo correoElectronico;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 64)
    private String rol;

    protected AuthUsuarioEntity() {}

    public AuthUsuarioEntity(String id, String email, String passwordHash, String rol) {
        this.id = id;
        this.correoElectronico = new EmailCorporativo(email);
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public String getId() {
        return id;
    }

    /** Equivalente VO → primitivo de dominio para JWT y APIs. */
    public String getEmail() {
        return correoElectronico != null ? correoElectronico.getValor() : null;
    }

    public EmailCorporativo getCorreoElectronico() {
        return correoElectronico;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRol() {
        return rol;
    }
}
