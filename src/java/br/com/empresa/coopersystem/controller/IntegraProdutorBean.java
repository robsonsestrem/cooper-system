package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.ProdutorDAO;
import br.com.empresa.coopersystem.model.Produtor;
import br.com.empresa.coopersystem.util.Messages;
import br.com.empresa.coopersystem.util.SessionUtils;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBintegraProdutor")
@ViewScoped
public class IntegraProdutorBean implements Serializable {

    //valor entre 0 a 100 que sera usado no progressbar
    private Integer progresso;

    //mensagem de dados sendo processados 
    private String mensagem;

    //quantidade de dados para inserir
    private Integer quantidadeDados;

    //carregar dataTable
    private List<Produtor> dadosColetados;

    public String limpar() {
        HttpSession session = SessionUtils.getSession();
        session.isNew();
        return "/integracao/cargaProdutores.xhtml?faces-redirect=true";
    }

    //reseta o progresso e a mensagem do progressbar
    public void resetarProgresso() {
        progresso = null;
        mensagem = "";
    }

    /**
     * atualiza o progresso
     *
     * @param i posicao do dado na lista
     */
    private void atualizarProgresso(int i) {
        //calculo para o percentual do processo em relacao a quantidade de dados
        progresso = (i * 100) / quantidadeDados;
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    //condicionais para determinadas ações
    public void integrarProdutores(int acao) {
        try {
            switch (acao) {
                //integra dados dos produtores
                case 1:
                    //carrega lista com os dados
                    ProdutorDAO matriculas = new ProdutorDAO();
                    dadosColetados = new ArrayList<>();
                    dadosColetados = matriculas.produtoresNaoIntegrados();
                    //traz quantidade para ser inserida
                    ProdutorDAO qtdadeInserida = new ProdutorDAO();
                    quantidadeDados = qtdadeInserida.countIntegraProdutores();
                    if (quantidadeDados != 0) {

                        //"for" otimizado para o valor de i não ficar com zero para divisão
                        for (int i = 1; i <= dadosColetados.size(); i++) {
                            //mensagem = " " + dadosColetados.get(i).getMatricula() + "";
                            mensagem = " Processando dado nº " + i + " do total de " + quantidadeDados;
                            atualizarProgresso(i);
                        }
                        //Realiza inserção
                        ProdutorDAO integra = new ProdutorDAO();
                        integra.insereProdNaoIntegrado();
                        Messages.adicionarMensagemSucesso("Total de Matrículas Integradas: " + quantidadeDados);
                    } else {
                        dadosColetados = new ArrayList<>();
                        Messages.adicionarMensagemAlerta("Sem Dados para Integrar");
                    }
                    break;
                default:
                    break;
            }
            resetarProgresso();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public Integer getProgresso() {
        return progresso;
    }

    public void setProgresso(Integer progresso) {
        this.progresso = progresso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getQuantidadeDados() {
        return quantidadeDados;
    }

    public void setQuantidadeDados(Integer quantidadeDados) {
        this.quantidadeDados = quantidadeDados;
    }

    public List<Produtor> getDadosColetados() {
        return dadosColetados;
    }

    public void setDadosColetados(List<Produtor> dadosColetados) {
        this.dadosColetados = dadosColetados;
    }

}
