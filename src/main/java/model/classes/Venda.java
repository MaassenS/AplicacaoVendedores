package model.classes;

import java.time.LocalDate;

public class Venda {
    private int id;
    private int produtoId;
    private int quantidade;
    private LocalDate dataVenda;
    private Produto produto; // Para JOIN com produtos

    // Construtor vazio
    public Venda() {}

    // Construtor completo
    public Venda(int id, int produtoId, int quantidade, LocalDate dataVenda) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.dataVenda = dataVenda;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProdutoId() { return produtoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public LocalDate getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    // Métodos auxiliares para cálculos (usados nos relatórios)
    public double getValorTotal() {
        if (produto != null) {
            return produto.getPrecoVenda() * quantidade;
        }
        return 0.0;
    }

    public double getLucroTotal() {
        if (produto != null) {
            return produto.getLucroUnitario() * quantidade;
        }
        return 0.0;
    }

    // Métodos para exibição na tabela
    public String getNomeProduto() {
        return produto != null ? produto.getNome() : "";
    }

    public String getCodigoProduto() {
        return produto != null ? produto.getCodigo() : "";
    }

    public String getLinhaProduto() {
        return produto != null ? produto.getLinha() : "";
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", quantidade=" + quantidade +
                ", dataVenda=" + dataVenda +
                '}';
    }
}
