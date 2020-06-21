package br.com.empresa.coopersystem.model;

import static com.sun.faces.facelets.tag.jstl.fn.JstlFunction.toUpperCase;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ROBSON SESTREM
 * 
 */

//classe usada para campo transiente
public class TipoAtividade implements Serializable {

    private String tipo;

    public TipoAtividade() {
        this.tipo = "";
    }

    public TipoAtividade(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return toUpperCase(tipo);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.tipo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoAtividade other = (TipoAtividade) obj;
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tipo;
    }

}
