package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.util.SessionUtils;
import br.com.empresa.coopersystem.dao.LoginDAO;
import br.com.empresa.coopersystem.model.Usuario;
import br.com.empresa.coopersystem.model.UsuarioGrupoAcesso;
import br.com.empresa.coopersystem.util.Messages;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBlogin")
@SessionScoped
public class LoginBean implements Serializable {

    private String nomeLogin;
    private String senha;
    Usuario logado = new Usuario();
    List<UsuarioGrupoAcesso> acessos = new ArrayList<>();
    private LocalDate hoje;

    @PostConstruct
    public void dadosAtualizados() {
        //carrega data dos direitos reservados
        this.hoje = LocalDate.now();
    }

    //validando login
    //busca de dados para tratar as regras de negócio
    public String validateUsernamePassword() {
        boolean user = false;
        try {
            user = LoginDAO.validate(nomeLogin, senha);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
        if (user) {
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", user);

            //busca dos acessos
            LoginDAO buscaAcessos = new LoginDAO();
            try {
                acessos = buscaAcessos.buscaGrupoAcesso(nomeLogin);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Messages.adicionarMensagemErro(ex.getMessage());
            }

            //busca de dados do usuário
            LoginDAO buscaUser = new LoginDAO();
            try {
                logado = buscaUser.usuarioDB(nomeLogin);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Messages.adicionarMensagemErro(ex.getMessage());
            }

            return "/home.xhtml?faces-redirect=true";
        } else {
            Messages.adicionarMensagemAlerta("Login Incorreto: Verifique Usuário ou Senha!");
            return null;
        }
    }

    //realizar logout
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "/login.xhtml?faces-redirect=true";
    }

    //voltar para home
    public String home() {
        HttpSession session = SessionUtils.getSession();
        session.isNew();
        return "/home.xhtml?faces-redirect=true";
    }

    //construir senha
    public String buildPassword() {
        //HttpSession session = SessionUtils.getSession();
        //session.isNew();
        return "/buildPassword.xhtml?faces-redirect=true";
    }

    //voltar a página de entrada
    public String returnLogin() {
        HttpSession session = SessionUtils.getSession();
        session.isNew();
        return "/home.xhtml?faces-redirect=true";
    }

    public String getNomeLogin() {
        return nomeLogin;
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

    public Usuario getLogado() {
        return logado;
    }

    public void setLogado(Usuario logado) {
        this.logado = logado;
    }

    public List<UsuarioGrupoAcesso> getAcessos() {
        return acessos;
    }

    public void setAcessos(List<UsuarioGrupoAcesso> acessos) {
        this.acessos = acessos;
    }

    public LocalDate getHoje() {
        return hoje;
    }

    public void setHoje(LocalDate hoje) {
        this.hoje = hoje;
    }

}
