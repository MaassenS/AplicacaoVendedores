/*
 * DAO para Produto - baseado no VendedorDao.java
 * Adaptado para a tabela produtos do banco inelta_sistema
 */
package model.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.DB.DB;
import model.classes.Produto;

/**
 * Data Access Object para Produto
 * @author [seu_nome]
 */
public class ProdutoDao {

    private Connection con;

    public ProdutoDao(Connection con) {
        this.con = con;
    }

    /**
     * Busca todos os produtos - similar ao getAll() do VendedorDao
     */
    public List<Produto> getAll() {
        List<Produto> list = new ArrayList<>();
        ResultSet res = null;
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT * FROM produtos ORDER BY linha, nome";
            stmt = con.prepareStatement(sql);
            res = stmt.executeQuery();

            while (res.next()) {
                Produto produto = new Produto(
                        res.getInt("id"),
                        res.getString("codigo"),
                        res.getString("nome"),
                        res.getString("linha"),
                        res.getDouble("precoCusto"),
                        res.getDouble("precoVenda"),
                        res.getDouble("peso_kg"),
                        res.getBoolean("ativo")
                );

                System.out.println(produto.toString());
                list.add(produto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(res);
            DB.closeStatement(stmt);
        }

        return list;
    }

    /**
     * Insere novo produto - adaptado do inserir() do VendedorDao
     */
    public boolean inserir(Produto produto) {
        PreparedStatement stmt = null;
        boolean result = false;

        try {
            String sql = "INSERT INTO produtos (codigo, nome, linha, precoCusto, " +
                    "precoVenda, peso_kg, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";

            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getLinha());
            stmt.setDouble(4, produto.getPrecoCusto());
            stmt.setDouble(5, produto.getPrecoVenda());
            stmt.setDouble(6, produto.getPeso_kg());
            stmt.setBoolean(7, produto.isAtivo());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    produto.setId(id);
                    result = true;
                }
            } else {
                throw new SQLException("Não foi possível inserir produto");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(stmt);
        }

        return result;
    }

    /**
     * Edita produto existente - adaptado do editar() do VendedorDao
     */
    public boolean editar(Produto produto) {
        PreparedStatement stmt = null;
        boolean result = false;

        try {
            String sql = "UPDATE produtos SET codigo = ?, nome = ?, linha = ?, " +
                    "precoCusto = ?, precoVenda = ?, peso_kg = ?, ativo = ? " +
                    "WHERE id = ?";

            stmt = con.prepareStatement(sql);
            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getLinha());
            stmt.setDouble(4, produto.getPrecoCusto());
            stmt.setDouble(5, produto.getPrecoVenda());
            stmt.setDouble(6, produto.getPeso_kg());
            stmt.setBoolean(7, produto.isAtivo());
            stmt.setInt(8, produto.getId());

            stmt.executeUpdate();
            result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(stmt);
        }

        return result;
    }

    /**
     * Exclui produto - adaptado do excluir() do VendedorDao
     */
    public boolean excluir(Produto produto) {
        PreparedStatement stmt = null;
        boolean result = false;

        try {
            String sql = "DELETE FROM produtos WHERE id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, produto.getId());
            stmt.executeUpdate();
            result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(stmt);
        }

        return result;
    }

    // ========================================
    // MÉTODOS ESPECÍFICOS PARA ANÁLISE DE LUCRO
    // ========================================

    /**
     * Busca produtos mais vendidos (JOIN com vendas)
     */
    public List<Produto> getProdutosMaisVendidos(int limite) {
        List<Produto> list = new ArrayList<>();
        ResultSet res = null;
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT p.*, SUM(v.quantidade) as total_vendido " +
                    "FROM produtos p " +
                    "INNER JOIN vendas v ON p.id = v.produto_id " +
                    "GROUP BY p.id " +
                    "ORDER BY total_vendido DESC " +
                    "LIMIT ?";

            stmt = con.prepareStatement(sql);
            stmt.setInt(1, limite);
            res = stmt.executeQuery();

            while (res.next()) {
                Produto produto = new Produto(
                        res.getInt("id"),
                        res.getString("codigo"),
                        res.getString("nome"),
                        res.getString("linha"),
                        res.getDouble("precoCusto"),
                        res.getDouble("precoVenda"),
                        res.getDouble("peso_kg"),
                        res.getBoolean("ativo")
                );

                list.add(produto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(res);
            DB.closeStatement(stmt);
        }

        return list;
    }

    /**
     * Busca produtos por linha (para filtros)
     */
    public List<Produto> getProdutosPorLinha(String linha) {
        List<Produto> list = new ArrayList<>();
        ResultSet res = null;
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT * FROM produtos WHERE linha = ? AND ativo = true " +
                    "ORDER BY nome";

            stmt = con.prepareStatement(sql);
            stmt.setString(1, linha);
            res = stmt.executeQuery();

            while (res.next()) {
                Produto produto = new Produto(
                        res.getInt("id"),
                        res.getString("codigo"),
                        res.getString("nome"),
                        res.getString("linha"),
                        res.getDouble("precoCusto"),
                        res.getDouble("precoVenda"),
                        res.getDouble("peso_kg"),
                        res.getBoolean("ativo")
                );

                list.add(produto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(res);
            DB.closeStatement(stmt);
        }

        return list;
    }
}
