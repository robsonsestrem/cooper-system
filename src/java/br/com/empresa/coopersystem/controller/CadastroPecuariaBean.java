package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.PecuariaDAO;
import br.com.empresa.coopersystem.model.Atividade;
import br.com.empresa.coopersystem.model.Pecuaria;
import br.com.empresa.coopersystem.model.Produtor;
import br.com.empresa.coopersystem.util.Messages;
import br.com.empresa.coopersystem.util.SessionUtils;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBpecuaria")
@ViewScoped
public class CadastroPecuariaBean implements Serializable {

    private Pecuaria oPecuaria = new Pecuaria();
    List<Pecuaria> prodPecuarias;

    @ManagedProperty(value = "#{MBlogin}")
    private LoginBean oLoginBean;

    @PostConstruct
    public void listarProdPecuaria() {
        try {
            this.prodPecuarias = new ArrayList<>();
            PecuariaDAO busca = new PecuariaDAO();
            prodPecuarias = busca.listaProdPecuaria(oLoginBean.getNomeLogin());
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void adicionarDadosPecuaria() {
        try {
            PecuariaDAO verifica = new PecuariaDAO();
            oPecuaria.setoUsuario(oLoginBean.getLogado());

            //Atenção - objetos aqui validados são os de "formulário"(oPecuaria.getoAtividade().getProducao()) que vão para o banco
            boolean existe = verifica.validarProdPecuaria(oPecuaria.getoProdutor().getMatricula(), oPecuaria.getoAtividade().getProducao(), oPecuaria.getQtdadeAnimais());

            if (existe) {
                Messages.adicionarMensagemAlerta("Estes Dados Já Foram Informados!");
                //atualização do datatable              
                PecuariaDAO atualizaTable = new PecuariaDAO();
                this.prodPecuarias = atualizaTable.listaProdPecuaria(oLoginBean.getNomeLogin());
            } else {
                PecuariaDAO insere = new PecuariaDAO();
                insere.adicionar(oPecuaria);
                Messages.adicionarMensagemSucesso("Dados Inseridos com Sucesso!");
                //atualização do datatable              
                PecuariaDAO atualizaTable = new PecuariaDAO();
                this.prodPecuarias = atualizaTable.listaProdPecuaria(oLoginBean.getNomeLogin());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public String cancelarLimpar() {
        //oPecuaria = new Pecuaria();                
        HttpSession session = SessionUtils.getSession();
        session.isNew();
        return "/producao/cadastroPecuaria.xhtml?faces-redirect=true";
    }

    public void onRowEditPecuaria(RowEditEvent event) {
        try {
            this.oPecuaria = (Pecuaria) event.getObject();
            PecuariaDAO verifica = new PecuariaDAO();

            //Atenção - objetos aqui validados são os de retorno no datatable que vão para o banco
            boolean existe = verifica.validarProdPecuaria(oPecuaria.getMatriculaSelect(), oPecuaria.getProducaoSelect(), oPecuaria.getQtdadeAnimais());

            //Aqui são tratados os valores dos campos com a mesma regra para "adicionar"
            if (existe || oPecuaria.getQtdadeAnimais() < 1 || oPecuaria.getQtdadeAnimais() > 99999) {
                Messages.adicionarMensagemAlerta("Estes Dados Já Foram Informados ou São Inválidos!");
                //atualização do datatable              
                PecuariaDAO atualizaTable = new PecuariaDAO();
                this.prodPecuarias = atualizaTable.listaProdPecuaria(oLoginBean.getNomeLogin());
            } else {
                PecuariaDAO editar = new PecuariaDAO();
                editar.editarPecuaria(oPecuaria);
                Messages.adicionarMensagemSucesso("Alterado com Sucesso!");
                //atualização do datatable              
                PecuariaDAO atualizaTable = new PecuariaDAO();
                this.prodPecuarias = atualizaTable.listaProdPecuaria(oLoginBean.getNomeLogin());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void onRowCancelPecuaria(RowEditEvent event) {
        Messages.adicionarMensagemSucesso("Edição cancelada.");
    }

    public void excluirProdPecuaria(ActionEvent evento) {
        try {
            this.oPecuaria = (Pecuaria) evento.getComponent().getAttributes().get("dadoSelecionado");
            PecuariaDAO exclui = new PecuariaDAO();
            exclui.excluirProPecuaria(oPecuaria);
            Messages.adicionarMensagemSucesso("Dados Excluídos Com Sucesso!");
            //atualização do datatable
            PecuariaDAO atualizaDatatable = new PecuariaDAO();
            this.prodPecuarias = atualizaDatatable.listaProdPecuaria(oLoginBean.getNomeLogin());

        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void produtorSelecionado(SelectEvent event) {
        Produtor oProdutor = (Produtor) event.getObject();
        oPecuaria.setoProdutor(oProdutor);
    }

    @NotBlank
    public String getNomeProdutor() {
        return oPecuaria.getoProdutor() == null ? null : oPecuaria.getoProdutor().getNome();
    }

    public void setNomeProdutor(String nomeProdutor) {
        //método feito para apenas não dar erro na busca de set para o objeto Produtor
    }

    public void atividadeSelecionada(SelectEvent event) {
        Atividade oAtividade = (Atividade) event.getObject();
        oPecuaria.setoAtividade(oAtividade);
    }

    @NotBlank
    public String getProducaoAtividade() {
        return oPecuaria.getoAtividade() == null ? null : oPecuaria.getoAtividade().getProducao();
    }

    public void setProducaoAtividade(String producaoAtividade) {
        //método feito para apenas não dar erro na busca de set para o objeto Atividade
    }

////////////////////////////////////////////////////////////////////////////////////
    public Pecuaria getoPecuaria() {
        return oPecuaria;
    }

    public void setoPecuaria(Pecuaria oPecuaria) {
        this.oPecuaria = oPecuaria;
    }

    public List<Pecuaria> getProdPecuarias() {
        return prodPecuarias;
    }

    public void setProdPecuarias(List<Pecuaria> prodPecuarias) {
        this.prodPecuarias = prodPecuarias;
    }

    public LoginBean getoLoginBean() {
        return oLoginBean;
    }

    public void setoLoginBean(LoginBean oLoginBean) {
        this.oLoginBean = oLoginBean;
    }

}
