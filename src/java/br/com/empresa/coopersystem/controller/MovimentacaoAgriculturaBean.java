package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.AgriculturaDAO;
import br.com.empresa.coopersystem.dao.LoginDAO;
import br.com.empresa.coopersystem.model.Agricultura;
import br.com.empresa.coopersystem.model.UsuarioGrupoAcesso;
import br.com.empresa.coopersystem.util.Messages;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBmovAgricultura")
@ViewScoped
public class MovimentacaoAgriculturaBean implements Serializable {

    private Agricultura oAgricultura = new Agricultura();
    List<Agricultura> prodAgriculturaAdmin;

    @ManagedProperty(value = "#{MBlogin}")
    private LoginBean oLoginBean;

    public LoginBean getoLoginBean() {
        return oLoginBean;
    }

    public void setoLoginBean(LoginBean oLoginBean) {
        this.oLoginBean = oLoginBean;
    }

    public List<UsuarioGrupoAcesso> acessos() {
        List<UsuarioGrupoAcesso> dados = new ArrayList<>();
        LoginDAO busca = new LoginDAO();
        try {
            dados = busca.buscaGrupoAcesso(oLoginBean.logado.getNomeLogin());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dados;
    }

    /*    
     Aplicado as regras de permissão 
     */
    public String informativoPermissao() {
        boolean validaAcesso = false;
        for (int i = 0; i < oLoginBean.acessos.size(); i++) {
            if (oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("GERENTEADM")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("ADMIN")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("DIRETORIA")) {
                validaAcesso = true;
            }
        }
        if (!validaAcesso) {
            return "Usuário sem privilégios nesta página.";
        } else {
            return null;
        }
    }

    /*    
     Aplicado as regras de permissão 
     */
    @PostConstruct
    public void listarProdAgricultura() {
        boolean validaAcesso = false;
        for (int i = 0; i < oLoginBean.acessos.size(); i++) {
            if (oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("GERENTEADM")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("ADMIN")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("DIRETORIA")) {
                validaAcesso = true;
            }
        }
        if (validaAcesso) {
            this.prodAgriculturaAdmin = new ArrayList<>();
            //atualização do datatable
            AgriculturaDAO busca = new AgriculturaDAO();
            try {
                this.prodAgriculturaAdmin = busca.listaProdAgriculturaAdmin();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Messages.adicionarMensagemErro(ex.getMessage());
            }
        }
    }

    public void onRowEditAgricultura(RowEditEvent event) {
        try {
            this.oAgricultura = (Agricultura) event.getObject();
            AgriculturaDAO verifica = new AgriculturaDAO();

            //Atenção - objetos aqui validados são os de retorno no datatable que vão para o banco
            boolean existe = verifica.validarProdAgricultura(oAgricultura.getMatriculaSelect(), oAgricultura.getProducaoSelect(),
                    oAgricultura.getAreaCultivo(), oAgricultura.getAreaArrendada(), oAgricultura.getSafra());

            //Aqui são tratados os valores dos campos com a mesma regra para "adicionar"
            if (existe || oAgricultura.getAreaCultivo() < 1.00 || oAgricultura.getAreaCultivo() > 999999.99) {
                Messages.adicionarMensagemAlerta("Estes Dados Já Foram Informados ou são inválidos");
                //atualização do datatable              
                AgriculturaDAO atualizaTable = new AgriculturaDAO();
                this.prodAgriculturaAdmin = atualizaTable.listaProdAgriculturaAdmin();
            } else {
                AgriculturaDAO editar = new AgriculturaDAO();
                editar.editarProdAgricultura(oAgricultura);
                Messages.adicionarMensagemSucesso("Alterado com Sucesso!");
                //atualização do datatable              
                AgriculturaDAO atualizaTable = new AgriculturaDAO();
                this.prodAgriculturaAdmin = atualizaTable.listaProdAgriculturaAdmin();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void onRowCancelAgricultura(RowEditEvent event) {
        try {
            Messages.adicionarMensagemSucesso("Edição cancelada.");
            //atualização do datatable              
            AgriculturaDAO atualizaTable = new AgriculturaDAO();
            this.prodAgriculturaAdmin = atualizaTable.listaProdAgriculturaAdmin();
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    public void excluirProdAgricultura(ActionEvent evento) {
        try {
            this.oAgricultura = (Agricultura) evento.getComponent().getAttributes().get("dadoSelecionado");
            AgriculturaDAO exclui = new AgriculturaDAO();
            exclui.excluirProdAgricultura(oAgricultura);
            Messages.adicionarMensagemSucesso("Dados Excluídos Com Sucesso!");
            //atualização do datatable
            AgriculturaDAO busca = new AgriculturaDAO();
            this.prodAgriculturaAdmin = busca.listaProdAgriculturaAdmin();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public Agricultura getoAgricultura() {
        return oAgricultura;
    }

    public void setoAgricultura(Agricultura oAgricultura) {
        this.oAgricultura = oAgricultura;
    }

    public List<Agricultura> getProdAgriculturaAdmin() {
        return prodAgriculturaAdmin;
    }

    public void setProdAgriculturaAdmin(List<Agricultura> prodAgriculturaAdmin) {
        this.prodAgriculturaAdmin = prodAgriculturaAdmin;
    }

}
