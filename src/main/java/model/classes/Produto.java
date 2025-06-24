package model.classes;

import java.time.LocalDateTime;

public class Produto {
    private int id;
    private String codigo;
    private String nome;
    private String linha;
    private double precoCusto;
    private double precoVenda;
    private double peso_kg;
    private boolean ativo;

    // Construtor vazio
    public Produto() {}

    // Construtor completo (ESTE É O QUE ESTÁ FALTANDO!)
    public Produto(int id, String codigo, String nome, String linha,
                   double precoCusto, double precoVenda, double peso_kg, boolean ativo) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.linha = linha;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.peso_kg = peso_kg;
        this.ativo = ativo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLinha() { return linha; }
    public void setLinha(String linha) { this.linha = linha; }

    public double getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(double precoCusto) { this.precoCusto = precoCusto; }

    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }

    public double getPeso_kg() { return peso_kg; }
    public void setPeso_kg(double peso_kg) { this.peso_kg = peso_kg; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    // Métodos para cálculos de lucro
    public double getLucroUnitario() {
        return precoVenda - precoCusto;
    }

    public double getMargemLucro() {
        if (precoVenda > 0) {
            return (getLucroUnitario() / precoVenda) * 100;
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return nome; // Para exibir em ComboBox
    }
}
