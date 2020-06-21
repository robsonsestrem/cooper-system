package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.AtividadeDAO;
import br.com.empresa.coopersystem.model.Atividade;
import br.com.empresa.coopersystem.util.Messages;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBselecaoAtividadePecuaria")
@ViewScoped
public class SelecaoAtividadePecuariaBean implements Serializable {

    private String producao; //variável de pesquisa    
    private List<Atividade> atividadesFiltradas;

    public void pesquisar() {
        try {
            AtividadeDAO atividades = new AtividadeDAO();
            atividadesFiltradas = atividades.pesquisaAtividadePecuaria(producao);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void selecionar(Atividade oAtividade) {
        RequestContext.getCurrentInstance().closeDialog(oAtividade);
    }

    public void abrirDialogo() {
        Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);
        opcoes.put("contentHeight", 500);
        //opcoes.put("draggable", true);
        //opcoes.put("responsive", true);
        //opcoes.put("dynamic", true);
        //opcoes.put("position", "top"); não funciona
        RequestContext.getCurrentInstance().openDialog("selecaoAtividadePecuaria", opcoes, null);
    }

    public String getProducao() {
        return producao;
    }

    public void setProducao(String producao) {
        this.producao = producao;
    }

    public List<Atividade> getAtividadesFiltradas() {
        return atividadesFiltradas;
    }

    public void setAtividadesFiltradas(List<Atividade> atividadesFiltradas) {
        this.atividadesFiltradas = atividadesFiltradas;
    }
}
