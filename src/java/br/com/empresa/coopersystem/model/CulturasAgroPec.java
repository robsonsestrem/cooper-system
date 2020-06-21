package br.com.empresa.coopersystem.model;

import static com.sun.faces.facelets.tag.jstl.fn.JstlFunction.toUpperCase;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ROBSON SESTREM
 */

//classe usada para campo transiente
public class CulturasAgroPec implements Serializable {

    private String culturasAgroPec;

    public CulturasAgroPec() {
        this.culturasAgroPec = "";
    }

    public CulturasAgroPec(String culturasAgroPec) {
        this.culturasAgroPec = culturasAgroPec;
    }

    public String getCulturasAgroPec() {
        return toUpperCase(culturasAgroPec);
    }

    public void setCulturasAgroPec(String culturasAgroPec) {
        this.culturasAgroPec = culturasAgroPec;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.culturasAgroPec);
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
        final CulturasAgroPec other = (CulturasAgroPec) obj;
        if (!Objects.equals(this.culturasAgroPec, other.culturasAgroPec)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return culturasAgroPec;
    }

}
