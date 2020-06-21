package br.com.empresa.coopersystem.model;

import java.io.Serializable;

/**
 *
 * @author ROBSON SESTREM
 */

public class Pecuaria implements Serializable {

    private int idProdPecuaria;
    private Produtor oProdutor;
    private Atividade oAtividade;
    private Usuario oUsuario;
    private int qtdadeAnimais;

    //atributos usados para "select" personalizado
    private String usuarioSelect;
    private String dataInsert;
    private int matriculaSelect;
    private String cultura;
    private String producaoSelect;
    private String nomeProselect;

    public int getIdProdPecuaria() {
        return idProdPecuaria;
    }

    public void setIdProdPecuaria(int idProdPecuaria) {
        this.idProdPecuaria = idProdPecuaria;
    }

    public Produtor getoProdutor() {
        return oProdutor;
    }

    public void setoProdutor(Produtor oProdutor) {
        this.oProdutor = oProdutor;
    }

    public Atividade getoAtividade() {
        return oAtividade;
    }

    public void setoAtividade(Atividade oAtividade) {
        this.oAtividade = oAtividade;
    }

    public Usuario getoUsuario() {
        return oUsuario;
    }

    public void setoUsuario(Usuario oUsuario) {
        this.oUsuario = oUsuario;
    }

    public int getQtdadeAnimais() {
        return qtdadeAnimais;
    }

    public void setQtdadeAnimais(int qtdadeAnimais) {
        this.qtdadeAnimais = qtdadeAnimais;
    }

    public String getUsuarioSelect() {
        return usuarioSelect;
    }

    public void setUsuarioSelect(String usuarioSelect) {
        this.usuarioSelect = usuarioSelect;
    }

    public String getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(String dataInsert) {
        this.dataInsert = dataInsert;
    }

    public int getMatriculaSelect() {
        return matriculaSelect;
    }

    public void setMatriculaSelect(int matriculaSelect) {
        this.matriculaSelect = matriculaSelect;
    }

    public String getCultura() {
        return cultura;
    }

    public void setCultura(String cultura) {
        this.cultura = cultura;
    }

    public String getProducaoSelect() {
        return producaoSelect;
    }

    public void setProducaoSelect(String producaoSelect) {
        this.producaoSelect = producaoSelect;
    }

    public String getNomeProselect() {
        return nomeProselect;
    }

    public void setNomeProselect(String nomeProselect) {
        this.nomeProselect = nomeProselect;
    }

}
