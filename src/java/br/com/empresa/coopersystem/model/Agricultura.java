package br.com.empresa.coopersystem.model;

import java.io.Serializable;

/**
 *
 * @author ROBSON SESTREM
 */

public class Agricultura implements Serializable {

    private int idProdAgricultura;
    private Produtor oProdutor;
    private Atividade atividade;
    private Usuario oUsuario;
    private double areaCultivo;
    private double areaArrendada;
    private String safra;

    //atributos usados para "select" personalizado
    private String usuarioSelect;
    private String dataInsert;
    private int matriculaSelect;
    private String cultura;
    private String producaoSelect;
    private String nomeProSelect;

    public int getIdProdAgricultura() {
        return idProdAgricultura;
    }

    public void setIdProdAgricultura(int idProdAgricultura) {
        this.idProdAgricultura = idProdAgricultura;
    }

    public Produtor getoProdutor() {
        return oProdutor;
    }

    public void setoProdutor(Produtor oProdutor) {
        this.oProdutor = oProdutor;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(String dataInsert) {
        this.dataInsert = dataInsert;
    }

    public Usuario getoUsuario() {
        return oUsuario;
    }

    public void setoUsuario(Usuario oUsuario) {
        this.oUsuario = oUsuario;
    }

    public double getAreaCultivo() {
        return areaCultivo;
    }

    public void setAreaCultivo(double areaCultivo) {
        this.areaCultivo = areaCultivo;
    }

    public double getAreaArrendada() {
        return areaArrendada;
    }

    public void setAreaArrendada(double areaArrendada) {
        this.areaArrendada = areaArrendada;
    }

    public String getUsuarioSelect() {
        return usuarioSelect;
    }

    public void setUsuarioSelect(String usuarioSelect) {
        this.usuarioSelect = usuarioSelect;
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

    public String getNomeProSelect() {
        return nomeProSelect;
    }

    public void setNomeProSelect(String nomeProSelect) {
        this.nomeProSelect = nomeProSelect;
    }

    public String getSafra() {
        return safra;
    }

    public void setSafra(String safra) {
        this.safra = safra;
    }

}
