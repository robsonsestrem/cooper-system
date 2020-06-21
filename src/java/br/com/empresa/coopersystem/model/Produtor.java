package br.com.empresa.coopersystem.model;

import java.io.Serializable;

/**
 *
 * @author ROBSON SESTREM
 */

public class Produtor implements Serializable {

    private int matricula;
    private String nome;

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
