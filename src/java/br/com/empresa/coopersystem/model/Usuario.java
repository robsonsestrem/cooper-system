package br.com.empresa.coopersystem.model;

import static com.sun.faces.facelets.tag.jstl.fn.JstlFunction.toUpperCase;
import java.io.Serializable;

/**
 *
 * @author ROBSON SESTREM
 */

public class Usuario implements Serializable {

    private String nomeLogin;
    private String senha;
    private boolean situacao;
    private String email;

    public String getNomeLogin() {
        return toUpperCase(nomeLogin);
    }

    public void setNomeLogin(String nomeLogin) {
        this.nomeLogin = nomeLogin;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
