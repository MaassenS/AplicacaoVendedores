/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.DB.DB;
import model.classes.Departamento;
import model.classes.Vendedor;

/**
 *
 * @author herrmann
 */
public class VendedorDao {

    private Connection con;

    public VendedorDao(Connection con) {
        this.con = con;
    }

    public List<Vendedor> getAll() {
        List<Vendedor> list = new ArrayList<>();
        ResultSet res = null;
        PreparedStatement stmt = null;
        try {
            String sql = "select vendedor.*,departamento.nomedpto from vendedor "+
                         " join departamento on (departamento.coddpto = vendedor.coddpto)";
            stmt = con.prepareStatement(sql);

            res = stmt.executeQuery();
            while (res.next()) {
                Vendedor vend = new Vendedor(res.getInt("codvendedor"),
                                             res.getString("nomevendedor"),
                                             res.getDate("datanascimento").toLocalDate(),
                                             res.getDouble("salariobase"),
                                             new Departamento(res.getInt("coddpto"),
                                                              res.getString("nomedpto")));
                System.out.println(vend);
                list.add(vend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(res);
            DB.closeStatement(stmt);
            return list;
        }
    }

    public boolean inserir(Vendedor vend) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "insert into vendedor (nomevendedor,datanascimento,salariobase,coddpto) "+
                         " values (?,?,?,?)";
            stmt = con.prepareStatement(sql,
                                      Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, vend.getNomeVendedor());
            stmt.setDate(2, Date.valueOf(vend.getDataNascimento()));
            stmt.setDouble(3, vend.getSalarioBase());
            stmt.setInt(4, vend.getDpto().getCodDpto());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    vend.setCodVendedor(id);
                    result = true;
                }
            } else {
                throw new SQLException("Não foi possível inserir");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(stmt);
            return result;
        }
    }
    
    public boolean editar(Vendedor vend) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update vendedor set nomevendedor = ?, "+
                         " datanascimento = ?, "+
                         " salariobase = ?, "+
                         " coddpto = ? "+
                         " where codvendedor = ?";
            stmt = con.prepareStatement(sql);

            stmt.setString(1, vend.getNomeVendedor());
            stmt.setDate(2, Date.valueOf(vend.getDataNascimento()));
            stmt.setDouble(3, vend.getSalarioBase());
            stmt.setInt(4, vend.getDpto().getCodDpto());
            stmt.setInt(5, vend.getCodVendedor());

            stmt.executeUpdate();
            result = true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(stmt);
            return result;
        }
    }
    
    public boolean excluir(Vendedor vend) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "delete from vendedor where codvendedor = ?";
            stmt = con.prepareStatement(sql);

            stmt.setInt(1, vend.getCodVendedor());

            stmt.executeUpdate();
            result = true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(stmt);
            return result;
        }
    }   
}
