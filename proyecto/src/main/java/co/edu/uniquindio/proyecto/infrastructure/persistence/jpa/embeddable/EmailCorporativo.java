package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.embeddable;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object persistente encajado en una fila relacional (@Embedded).
 * Se persiste como columnas del agregado padre (sin tabla propia): modelo relacional compacto.
 */
@Embeddable
public class EmailCorporativo implements Serializable {

    private String valor;

    protected EmailCorporativo() {}

    public EmailCorporativo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    protected void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EmailCorporativo that && Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}
