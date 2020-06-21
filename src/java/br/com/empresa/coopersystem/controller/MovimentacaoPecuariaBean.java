package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.PecuariaDAO;
import br.com.empresa.coopersystem.model.Pecuaria;
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

@ManagedBean(name = "MBmovpecuaria")
@ViewScoped
public class MovimentacaoPecuariaBean implements Serializable {

    private Pecuaria oPecuaria = new Pecuaria();
    List<Pecuaria> prodPecuariasAdmin;

    @ManagedProperty(value = "#{MBlogin}")
    private LoginBean oLoginBean;

    public LoginBean getoLoginBean() {
        return oLoginBean;
    }

    public void setoLoginBean(LoginBean oLoginBean) {
        this.oLoginBean = oLoginBean;
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
    public void listarProdPecuaria() {
        boolean validaAcesso = false;
        for (int i = 0; i < oLoginBean.acessos.size(); i++) {
            if (oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("GERENTEADM")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("ADMIN")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("DIRETORIA")) {
                validaAcesso = true;
            }
        }
        if (validaAcesso) {
            this.prodPecuariasAdmin = new ArrayList<>();
            //atualização do datatable
            PecuariaDAO busca = new PecuariaDAO();
            try {
                this.prodPecuariasAdmin = busca.listaProdPecuariaAdmin();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Messages.adicionarMensagemErro(ex.getMessage());
            }
        }
    }

    public void onRowEditPecuaria(RowEditEvent event) {
        try {
            this.oPecuaria = (Pecuaria) event.getObject();
            PecuariaDAO verifica = new PecuariaDAO();

            //Atenção - objetos aqui validados são os de retorno no datatable que vão para o banco
            boolean existe = verifica.validarProdPecuaria(oPecuaria.getMatriculaSelect(), oPecuaria.getProducaoSelect(), oPecuaria.getQtdadeAnimais());

            //Aqui são tratados os valores dos campos com a mesma regra para "adicionar"
            if (existe || oPecuaria.getQtdadeAnimais() < 1 || oPecuaria.getQtdadeAnimais() > 99999) {
                Messages.adicionarMensagemAlerta("Estes Dados Já Foram Informados ou São Inválidos");
                //atualização do datatable              
                PecuariaDAO atualizaTable = new PecuariaDAO();
                this.prodPecuariasAdmin = atualizaTable.listaProdPecuariaAdmin();
            } else {
                PecuariaDAO editar = new PecuariaDAO();
                editar.editarPecuaria(oPecuaria);
                Messages.adicionarMensagemSucesso("Alterado com Sucesso!");
                //atualização do datatable              
                PecuariaDAO atualizaTable = new PecuariaDAO();
                this.prodPecuariasAdmin = atualizaTable.listaProdPecuariaAdmin();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void onRowCancelPecuaria(RowEditEvent event) {
        try {
            Messages.adicionarMensagemSucesso("Edição cancelada.");
            //atualização do datatable              
            PecuariaDAO atualizaTable = new PecuariaDAO();
            this.prodPecuariasAdmin = atualizaTable.listaProdPecuariaAdmin();
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    public void excluirProdPecuaria(ActionEvent evento) {
        try {
            this.oPecuaria = (Pecuaria) evento.getComponent().getAttributes().get("dadoSelecionado");
            PecuariaDAO exclui = new PecuariaDAO();
            exclui.excluirProPecuaria(oPecuaria);
            Messages.adicionarMensagemSucesso("Dados Excluídos Com Sucesso!");
            //atualização do datatable
            PecuariaDAO busca = new PecuariaDAO();
            this.prodPecuariasAdmin = busca.listaProdPecuariaAdmin();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public Pecuaria getoPecuaria() {
        return oPecuaria;
    }

    public void setoPecuaria(Pecuaria oPecuaria) {
        this.oPecuaria = oPecuaria;
    }

    public List<Pecuaria> getProdPecuariasAdmin() {
        return prodPecuariasAdmin;
    }

    public void setProdPecuariasAdmin(List<Pecuaria> prodPecuariasAdmin) {
        this.prodPecuariasAdmin = prodPecuariasAdmin;
    }

}
