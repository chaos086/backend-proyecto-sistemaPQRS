package co.edu.uniquindio.proyecto.infrastructure.persistence;

/**
 * Perfiles para alternar implementación de repositorios de dominio (JPA vs memoria).
 */
public final class PersistenceProfiles {

    /** Persistencia relacional (H2 / producción). */
    public static final String JPA = "jpa";

    /** Implementación en memoria (pruebas locales / demos sin esquema). */
    public static final String MEMORY = "memory";

    private PersistenceProfiles() {}
}
