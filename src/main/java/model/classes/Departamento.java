/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.classes;

/**
 *
 * @author herrmann
 */
public class Departamento {
    private int codDpto;
    private String nomeDpto;

    public Departamento(int codDpto, String nomeDpto) {
        this.codDpto = codDpto;
        this.nomeDpto = nomeDpto;
    }

    public Departamento() {
    }

    
    
    public int getCodDpto() {
        return codDpto;
    }

    public void setCodDpto(int codDpto) {
        this.codDpto = codDpto;
    }

    public String getNomeDpto() {
        return nomeDpto;
    }

    public void setNomeDpto(String nomeDpto) {
        this.nomeDpto = nomeDpto;
    }

    @Override
    public String toString() {
        return nomeDpto;
    }

    public String toString2() {
        return "Departamento{" + "codDpto=" + codDpto + ", nomeDpto=" + nomeDpto + '}';
    }
    
    
}
