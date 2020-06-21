package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.AgriculturaDAO;
import br.com.empresa.coopersystem.model.Atividade;
import br.com.empresa.coopersystem.model.Agricultura;
import br.com.empresa.coopersystem.model.Produtor;
import br.com.empresa.coopersystem.util.Messages;
import br.com.empresa.coopersystem.util.SessionUtils;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.hibernate.validator.constraints.NotBlank;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBagricola")
@ViewScoped
public class CadastroAgriculturaBean implements Serializable {

    private boolean checkbox;
    private boolean mostrar;
    private Agricultura oAgricultura = new Agricultura();
    List<Agricultura> prodAgricultura;

    //opção selecionada do selectOneMenu
    private String safraOption;
    private List<String> safraOptions;

    @ManagedProperty(value = "#{MBlogin}")
    private LoginBean oLoginBean;

    @PostConstruct
    public void listarProdAgricultura() {
        try {
            //dados dinâmicos do selectOneMenu
            safraOptions = new ArrayList<String>();
            LocalDate hoje = LocalDate.now();
            LocalDate anoAnterior = hoje.minusYears(1);
            LocalDate anoSeguinte = hoje.plusYears(1);
            safraOptions.add(Integer.toString(anoAnterior.getYear()) + "/" + Integer.toString(anoAnterior.getYear()));
            safraOptions.add(Integer.toString(anoAnterior.getYear()) + "/" + Integer.toString(hoje.getYear()));
            safraOptions.add(Integer.toString(hoje.getYear()) + "/" + Integer.toString(hoje.getYear()));
            safraOptions.add(Integer.toString(hoje.getYear()) + "/" + Integer.toString(anoSeguinte.getYear()));
            safraOptions.add(Integer.toString(anoSeguinte.getYear()) + "/" + Integer.toString(anoSeguinte.getYear()));

            //mantém checkbox marcado
            checkbox = true;
            //inicializa inputText desabilitado
            mostrarInputText();
            //atualização do datatable
            this.prodAgricultura = new ArrayList<>();
            AgriculturaDAO busca = new AgriculturaDAO();
            this.prodAgricultura = busca.listaProdAgricultura(oLoginBean.getNomeLogin());
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void adicionarDadosAgricultura() {
        try {
            AgriculturaDAO verifica = new AgriculturaDAO();
            oAgricultura.setoUsuario(oLoginBean.getLogado());
            oAgricultura.setSafra(safraOption);

            //Atenção - objetos aqui validados são os de formulário que vão para o banco
            boolean existe = verifica.validarProdAgricultura(oAgricultura.getoProdutor().getMatricula(), oAgricultura.getAtividade().getProducao(),
                    oAgricultura.getAreaCultivo(), oAgricultura.getAreaArrendada(), oAgricultura.getSafra());
            if (existe) {
                Messages.adicionarMensagemAlerta("Estes Dados Já Foram Informados!");
                //atualização do datatable              
                AgriculturaDAO atualizaTable = new AgriculturaDAO();
                this.prodAgricultura = atualizaTable.listaProdAgricultura(oLoginBean.getNomeLogin());
            } else {
                if (checkbox) {
                    Messages.adicionarMensagemAlerta("Campo Arrendamento Obrigatório, Caso Não Tenha Informe Zero!");
                } else {
                    if (oAgricultura.getAreaCultivo() == 0.00 && oAgricultura.getAreaArrendada() == 0.00) {
                        Messages.adicionarMensagemErro("Favor informar algum valor de Área");
                    } else {

                        AgriculturaDAO insere = new AgriculturaDAO();
                        insere.adicionarProdAgricultura(oAgricultura);
                        Messages.adicionarMensagemSucesso("Dados Inseridos com Sucesso!");
                        //zerar arrendamento para o caso de o usuário esquecer preenchido nas próximas inserções
                        oAgricultura.setAreaArrendada(0);
                        //atualização do datatable              
                        AgriculturaDAO atualizaTable = new AgriculturaDAO();
                        this.prodAgricultura = atualizaTable.listaProdAgricultura(oLoginBean.getNomeLogin());
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public String cancelarLimpar() {
        HttpSession session = SessionUtils.getSession();
        session.isNew();
        return "/producao/cadastroAgricultura.xhtml?faces-redirect=true";
    }

    public void onRowEditAgricultura(RowEditEvent event) {
        try {
            this.oAgricultura = (Agricultura) event.getObject();
            AgriculturaDAO verifica = new AgriculturaDAO();

            //Atenção - objetos aqui validados são os de retorno no datatable que vão para o banco
            boolean existe = verifica.validarProdAgricultura(oAgricultura.getMatriculaSelect(), oAgricultura.getProducaoSelect(),
                    oAgricultura.getAreaCultivo(), oAgricultura.getAreaArrendada(), oAgricultura.getSafra());

            //Aqui são tratados os valores dos campos com a mesma regra para "adicionar"
            if (existe || oAgricultura.getAreaCultivo() < 0.00 || oAgricultura.getAreaCultivo() > 999999.99) {
                Messages.adicionarMensagemAlerta("Estes Dados Já Foram Informados ou São Inválidos!");
                //atualização do datatable              
                AgriculturaDAO atualizaTable = new AgriculturaDAO();
                this.prodAgricultura = atualizaTable.listaProdAgricultura(oLoginBean.getNomeLogin());
            } else {
                AgriculturaDAO editar = new AgriculturaDAO();
                editar.editarProdAgricultura(oAgricultura);
                Messages.adicionarMensagemSucesso("Alterado com Sucesso!");
                //atualização do datatable              
                AgriculturaDAO atualizaTable = new AgriculturaDAO();
                this.prodAgricultura = atualizaTable.listaProdAgricultura(oLoginBean.getNomeLogin());
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
            this.prodAgricultura = atualizaTable.listaProdAgricultura(oLoginBean.getNomeLogin());
        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void excluirProdAgricultura(ActionEvent evento) {
        try {
            this.oAgricultura = (Agricultura) evento.getComponent().getAttributes().get("dadoSelecionado");
            AgriculturaDAO exclui = new AgriculturaDAO();
            exclui.excluirProdAgricultura(oAgricultura);
            Messages.adicionarMensagemSucesso("Dados Excluídos Com Sucesso!");
            //atualização do datatable              
            AgriculturaDAO atualizaTable = new AgriculturaDAO();
            this.prodAgricultura = atualizaTable.listaProdAgricultura(oLoginBean.getNomeLogin());

        } catch (SQLException ex) {
            ex.printStackTrace();
            Messages.adicionarMensagemErro(ex.getMessage());
        }
    }

    public void mostrarInputText() {
        if (!checkbox) {
            mostrar = false;
        } else {
            mostrar = true;
        }
        //String summary = checkbox ? "Marcado" : "Desmarcado";
        //Messages.adicionarMensagemAlerta(summary);
    }

    public void produtorSelecionado(SelectEvent event) {
        Produtor oProdutor = (Produtor) event.getObject();
        oAgricultura.setoProdutor(oProdutor);
    }

    @NotBlank
    public String getNomeProdutor() {
        return oAgricultura.getoProdutor() == null ? null : oAgricultura.getoProdutor().getNome();
    }

    public void setNomeProdutor(String nomeProdutor) {
        //método feito para apenas não dar erro na busca de set para o objeto Produtor
    }

    public void atividadeSelecionada(SelectEvent event) {
        Atividade oAtividade = (Atividade) event.getObject();
        oAgricultura.setAtividade(oAtividade);
    }

    @NotBlank
    public String getProducaoAtividade() {
        return oAgricultura.getAtividade() == null ? null : oAgricultura.getAtividade().getProducao();
    }

    public void setProducaoAtividade(String producaoAtividade) {
        //método feito para apenas não dar erro na busca de set para o objeto Atividade
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Agricultura getoAgricultura() {
        return oAgricultura;
    }

    public void setoAgricultura(Agricultura oAgricultura) {
        this.oAgricultura = oAgricultura;
    }

    public LoginBean getoLoginBean() {
        return oLoginBean;
    }

    public void setoLoginBean(LoginBean oLoginBean) {
        this.oLoginBean = oLoginBean;
    }

    public List<Agricultura> getProdAgricultura() {
        return prodAgricultura;
    }

    public void setProdAgricultura(List<Agricultura> prodAgricultura) {
        this.prodAgricultura = prodAgricultura;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public String getSafraOption() {
        return safraOption;
    }

    public void setSafraOption(String safraOption) {
        this.safraOption = safraOption;
    }

    public List<String> getSafraOptions() {
        return safraOptions;
    }

    public void setSafraOptions(List<String> safraOptions) {
        this.safraOptions = safraOptions;
    }

}
