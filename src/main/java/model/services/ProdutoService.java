/*
 * Classe de Serviço para Produto - Sistema Inelta
 * Segue o mesmo padrão do VendedorService.java
 */
package model.services;

import java.util.List;
import model.DB.DB;
import model.classes.Produto;
import model.dao.ProdutoDao;

/**
 * Service para gerenciar produtos no sistema Inelta
 * @author Sistema Inelta
 */
public class ProdutoService {

    private ProdutoDao dao = new ProdutoDao(DB.getConnection());

    /**
     * Busca todos os produtos cadastrados
     * @return Lista de produtos
     */
    public List<Produto> getAll() {
        return dao.getAll();
    }

    /**
     * Salva ou atualiza um produto
     * @param produto Produto a ser salvo/atualizado
     * @return true se operação foi bem-sucedida
     */
    public boolean salvarOuAtualizar(Produto produto) {
        if (produto.getId() <= 0) {
            return dao.inserir(produto);
        } else {
            return dao.editar(produto);
        }
    }

    /**
     * Exclui um produto
     * @param produto Produto a ser excluído
     * @return true se operação foi bem-sucedida
     */
    public boolean excluir(Produto produto) {
        return dao.excluir(produto);
    }

    /**
     * Busca produtos por linha
     * @param linha Linha do produto (LINHA 471, LINHA 485, etc.)
     * @return Lista de produtos da linha especificada
     */
    public List<Produto> getProdutosPorLinha(String linha) {
        return dao.getProdutosPorLinha(linha);
    }

    /**
     * Busca produtos mais vendidos
     * @param limite Quantidade máxima de produtos a retornar
     * @return Lista dos produtos mais vendidos
     */
    public List<Produto> getProdutosMaisVendidos(int limite) {
        return dao.getProdutosMaisVendidos(limite);
    }

    /**
     * Calcula lucro total de um produto
     * @param produto Produto para calcular lucro
     * @param quantidadeVendida Quantidade vendida
     * @return Lucro total
     */
    public double calcularLucroTotal(Produto produto, int quantidadeVendida) {
        return produto.getLucroUnitario() * quantidadeVendida;
    }
}