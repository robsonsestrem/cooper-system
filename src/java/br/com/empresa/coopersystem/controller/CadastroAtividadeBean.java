package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.AtividadeDAO;
import br.com.empresa.coopersystem.model.Atividade;
import br.com.empresa.coopersystem.model.CulturasAgroPec;
import br.com.empresa.coopersystem.model.TipoAtividade;
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
import javax.servlet.http.HttpSession;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBcadastroAtividade")
@ViewScoped
public class CadastroAtividadeBean implements Serializable {

    private Atividade oAtividade = new Atividade();
    private List<Atividade> listaAtividades;

    private TipoAtividade tipoSelecionado;
    private List<TipoAtividade> tipoSelect;
    private CulturasAgroPec culturaSelecionada;
    private List<CulturasAgroPec> culturaSelect;

    @ManagedProperty(value = "#{MBlogin}")
    private LoginBean oLoginBean;

    @PostConstruct
    public void listarAtividades() {
        try {
            this.listaAtividades = new ArrayList<>();
            //atualização do datatable
            AtividadeDAO dao = new AtividadeDAO();
            listaAtividades = dao.listaAtividades();

            //carrega componente selectOneMenu
            AtividadeDAO buscaTipo = new AtividadeDAO();
            this.tipoSelect = buscaTipo.buscaTipoAtividade();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void adicionarDadosAtividade() {
        try {
            AtividadeDAO verifica = new AtividadeDAO();
            boolean existeProducao = verifica.validaProducao(oAtividade.getProducao());
            if (existeProducao) {
                Messages.adicionarMensagemAlerta("Produção informada já existe!");
                //atualização do datatable
                AtividadeDAO atualizaTable = new AtividadeDAO();
                listaAtividades = atualizaTable.listaAtividades();
            } else {
                oAtividade.setTipo(tipoSelecionado.getTipo());
                oAtividade.setCultura(culturaSelecionada.getCulturasAgroPec());
                oAtividade.setoUsuario(oLoginBean.getLogado());

                AtividadeDAO insere = new AtividadeDAO();
                insere.adicionarAtividade(oAtividade);
                Messages.adicionarMensagemSucesso("Atividade Inserida com Sucesso!");
                //atualização do datatable
                AtividadeDAO atualizaTable = new AtividadeDAO();
                listaAtividades = atualizaTable.listaAtividades();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public String cancelarLimpar() {
        oAtividade = new Atividade();
        HttpSession session = SessionUtils.getSession();
        session.isNew();
        return "/cadastro/cadastroAtividade.xhtml?faces-redirect=true";
    }

    public void onRowEditAtividade(RowEditEvent event) {
        try {
            Atividade oAtividade = (Atividade) event.getObject();
            AtividadeDAO verifica = new AtividadeDAO();
            boolean existeProducao = verifica.validaProducao(oAtividade.getProducao());

            //Aqui estão tratados os campos para receber somente "letras"
            //Seguindo a mesma regra no formulário
            if (!existeProducao && oAtividade.getProducao().matches("^[a-zA-Z0-9 ÁÂÃÀÇÉÊÍÓÔÕÚÜáâãàçéêíóôõúü]*$")) {
                AtividadeDAO daoEdita = new AtividadeDAO();
                daoEdita.EditarAtividade(oAtividade);
                Messages.adicionarMensagemSucesso("Alterado com Sucesso!");
                //atualiza datatable
                AtividadeDAO atualizaTable = new AtividadeDAO();
                listaAtividades = atualizaTable.listaAtividades();
            } else {
                Messages.adicionarMensagemAlerta("Dados Já Foram Informados ou São Inválidos!");
                //atualiza datatable
                AtividadeDAO atualizaTable = new AtividadeDAO();
                listaAtividades = atualizaTable.listaAtividades();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void onRowCancelAtividade(RowEditEvent event) {
        try {
            Messages.adicionarMensagemSucesso("Edição cancelada.");
            //atualiza datatable
            AtividadeDAO atualizaTable = new AtividadeDAO();
            listaAtividades = atualizaTable.listaAtividades();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public Atividade getoAtividade() {
        return oAtividade;
    }

    public void setoAtividade(Atividade oAtividade) {
        this.oAtividade = oAtividade;
    }

    public List<Atividade> getListaAtividades() {
        return listaAtividades;
    }

    public void setListaAtividades(List<Atividade> listaAtividades) {
        this.listaAtividades = listaAtividades;
    }

    public LoginBean getoLoginBean() {
        return oLoginBean;
    }

    public void setoLoginBean(LoginBean oLoginBean) {
        this.oLoginBean = oLoginBean;
    }

    public CulturasAgroPec getCulturaSelecionada() {
        return culturaSelecionada;
    }

    public void setCulturaSelecionada(CulturasAgroPec culturaSelecionada) {
        this.culturaSelecionada = culturaSelecionada;
    }

    public TipoAtividade getTipoSelecionado() {
        return tipoSelecionado;
    }

    public void setTipoSelecionado(TipoAtividade tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }

    public List<TipoAtividade> getTipoSelect() {
        return tipoSelect;
    }

    public void setTipoSelect(List<TipoAtividade> tipoSelect) {
        this.tipoSelect = tipoSelect;
    }

    public List<CulturasAgroPec> getCulturaSelect() {
        return culturaSelect;
    }

    public void setCulturaSelect(List<CulturasAgroPec> culturaSelect) {
        this.culturaSelect = culturaSelect;
    }

    //carga condicionada do evento ajax
    public void carregaCulturasAgroPec() {
        try {
            if (tipoSelecionado != null) {
                AtividadeDAO busca = new AtividadeDAO();
                culturaSelect = busca.buscaCulturasAgroPec(tipoSelecionado);
            } else {
                culturaSelect = new ArrayList<>();
            }
        } catch (RuntimeException rx) {
            Messages.adicionarMensagemErro(rx.getMessage());
        }
    }

}
