package br.com.empresa.coopersystem.model;

import static com.sun.faces.facelets.tag.jstl.fn.JstlFunction.toUpperCase;
import java.io.Serializable;

/**
 *
 * @author ROBSON SESTREM
 */

public class Atividade implements Serializable {

    private int IdAtividade;
    private String tipo;
    private String cultura;
    private String producao;
    private Usuario oUsuario;

    //atributo usado para "select" personalizado
    private String usuarioSelect;

    public int getIdAtividade() {
        return IdAtividade;
    }

    public void setIdAtividade(int IdAtividade) {
        this.IdAtividade = IdAtividade;
    }

    public String getTipo() {
        return toUpperCase(tipo);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCultura() {
        return toUpperCase(cultura);
    }

    public void setCultura(String cultura) {
        this.cultura = cultura;
    }

    public String getProducao() {
        return producao;
    }

    public void setProducao(String producao) {
        this.producao = producao;
    }

    public String getUsuarioSelect() {
        return usuarioSelect;
    }

    public void setUsuarioSelect(String usuarioSelect) {
        this.usuarioSelect = usuarioSelect;
    }

    public Usuario getoUsuario() {
        return oUsuario;
    }

    public void setoUsuario(Usuario oUsuario) {
        this.oUsuario = oUsuario;
    }

}
