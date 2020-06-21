package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.ProdutorDAO;
import br.com.empresa.coopersystem.model.Produtor;
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

@ManagedBean(name = "MBselecaoProdutor")
@ViewScoped
public class SelecaoProdutorBean implements Serializable {

    private String matriculaNome; //usado para passar valor de pesquisa para nome de produtores    
    private List<Produtor> produtoresFiltrados;

    public void pesquisar() throws SQLException {
        try {
            ProdutorDAO produtores = new ProdutorDAO();
            produtoresFiltrados = produtores.buscaCompletaProdutores(matriculaNome);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void selecionar(Produtor oProdutor) {
        RequestContext.getCurrentInstance().closeDialog(oProdutor);
    }

    public void abrirDialogo() {
        Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);
        opcoes.put("contentHeight", 500);
        //opcoes.put("draggable", true);
        //opcoes.put("responsive", true);
        //opcoes.put("dynamic", true);
        //opcoes.put("position", "relative"); //não funciona
        //opcoes.put("paginatorPosition", "botton"); //não funciona
        RequestContext.getCurrentInstance().openDialog("selecaoProdutor", opcoes, null);
    }

    public String getMatriculaNome() {
        return matriculaNome;
    }

    public void setMatriculaNome(String matriculaNome) {
        this.matriculaNome = matriculaNome;
    }

    public List<Produtor> getProdutoresFiltrados() {
        return produtoresFiltrados;
    }

    public void setProdutoresFiltrados(List<Produtor> produtoresFiltrados) {
        this.produtoresFiltrados = produtoresFiltrados;
    }
}
