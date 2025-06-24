package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DB.DB;
import model.classes.Produto;
import model.classes.Venda;

public class VendaDao {
    private Connection con;

    public VendaDao(Connection con) {
        this.con = con;
    }

    /**
     * Busca todas as vendas (sem dados do produto)
     * Para relatórios, use getVendasComProdutos()
     */
    public List<Venda> getAll() {
        List<Venda> list = new ArrayList<>();
        ResultSet res = null;
        PreparedStatement stmt = null;
        try {
            // ✅ CORRETO - Busca apenas dados da tabela vendas
            String sql = "SELECT id, produto_id, quantidade, data_venda FROM vendas ORDER BY data_venda DESC";
            stmt = con.prepareStatement(sql);
            res = stmt.executeQuery();

            while (res.next()) {
                Venda venda = new Venda(
                        res.getInt("id"),
                        res.getInt("produto_id"),
                        res.getInt("quantidade"),
                        res.getDate("data_venda").toLocalDate()
                );
                list.add(venda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DB.closeResultSet(res);
            DB.closeStatement(stmt);
        }
        return list;
    }

    /**
     * Busca vendas com dados dos produtos (JOIN) - Para relatórios
     */
    public List<Venda> getVendasComProdutos() {
        List<Venda> list = new ArrayList<>();
        ResultSet res = null;
        PreparedStatement stmt = null;
        try {
            // ✅ JOIN para trazer dados de produtos junto com vendas
            String sql = "SELECT v.id, v.produto_id, v.quantidade, v.data_venda, " +
                    "p.codigo, p.nome, p.linha, p.precoCusto, p.precoVenda, p.peso_kg, p.ativo " +
                    "FROM vendas v " +
                    "JOIN produtos p ON p.id = v.produto_id " +
                    "ORDER BY v.data_venda DESC";
            stmt = con.prepareStatement(sql);
            res = stmt.executeQuery();

            while (res.next()) {
                // Criar produto com dados do JOIN
                Produto produto = new Produto(
                        res.getInt("produto_id"),
                        res.getString("codigo"),
                        res.getString("nome"),
                        res.getString("linha"),
                        res.getDouble("precoCusto"),
                        res.getDouble("precoVenda"),
                        res.getDouble("peso_kg"),
                        res.getBoolean("ativo")
                );

                // Criar venda
                Venda venda = new Venda(
                        res.getInt("id"),
                        res.getInt("produto_id"),
                        res.getInt("quantidade"),
                        res.getDate("data_venda").toLocalDate()
                );
                venda.setProduto(produto); // Associar produto à venda

                list.add(venda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendas com produtos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DB.closeResultSet(res);
            DB.closeStatement(stmt);
        }
        return list;
    }
}
