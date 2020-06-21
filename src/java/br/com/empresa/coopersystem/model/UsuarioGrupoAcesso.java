package br.com.empresa.coopersystem.model;

import static com.sun.faces.facelets.tag.jstl.fn.JstlFunction.toUpperCase;
import java.io.Serializable;

/**
 *
 * @author ROBSON SESTREM
 */

public class UsuarioGrupoAcesso implements Serializable {

    private String grupoAcesso;

    public String getGrupoAcesso() {
        return toUpperCase(grupoAcesso);
    }

    public void setGrupoAcesso(String grupoAcesso) {
        this.grupoAcesso = grupoAcesso;
    }

}
