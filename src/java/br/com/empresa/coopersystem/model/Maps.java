package br.com.empresa.coopersystem.model;

import java.io.Serializable;

/**
 *
 * @author ROBSON SESTREM
 */

public class Maps implements Serializable {

    private String nome;
    private String latitude;
    private String longitude;
    private String cep;
    private String totalPessoaPorCep;

    public Maps() {
        this.nome = "";
        this.latitude = "";
        this.longitude = "";
        this.cep = "";
        this.totalPessoaPorCep = "";
    }

    public Maps(String nome, String latitude, String longitude, String cep, String totalPessoaPorCep) {
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cep = cep;
        this.totalPessoaPorCep = totalPessoaPorCep;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTotalPessoaPorCep() {
        return totalPessoaPorCep;
    }

    public void setTotalPessoaPorCep(String totalPessoaPorCep) {
        this.totalPessoaPorCep = totalPessoaPorCep;
    }

    public String toString() {
        return "Nome: " + getNome()
                + "\nLatitude: " + getLatitude()
                + "\nLongitude: " + getLongitude()
                + "\nCep: " + getCep()
                + "\nTotais por Cep: " + getTotalPessoaPorCep();

    }
}
