/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DB.DB;
import model.classes.Departamento;

/**
 *
 * @author herrmann
 */
public class DepartamentoDao {

    private Connection con;

    public DepartamentoDao(Connection con) {
        this.con = con;
    }

    public List<Departamento> getAll() {
        List<Departamento> list = new ArrayList<>();
        ResultSet res = null;
        PreparedStatement stmt = null;
        try {
            String sql = "select * from departamento";
            stmt = con.prepareStatement(sql);

            res = stmt.executeQuery();
            while (res.next()) {
                Departamento dpto = new Departamento(res.getInt("coddpto"),
                        res.getString("nomedpto"));
                System.out.println(dpto.toString2());
                list.add(dpto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(res);
            DB.closeStatement(stmt);
            return list;
        }

    }

    public boolean inserir(Departamento dpto) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "insert into departamento (nomedpto) values (?)";
            stmt = con.prepareStatement(sql,
                                      Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, dpto.getNomeDpto());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    dpto.setCodDpto(id);
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
    
    public boolean editar(Departamento dpto) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update departamento set nomedpto = ? where coddpto = ?";
            stmt = con.prepareStatement(sql);

            stmt.setString(1, dpto.getNomeDpto());
            stmt.setInt(2, dpto.getCodDpto());

            stmt.executeUpdate();
            result = true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(stmt);
            return result;
        }
    }
    
    public boolean excluir(Departamento dpto) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "delete from departamento where coddpto = ?";
            stmt = con.prepareStatement(sql);

            stmt.setInt(1, dpto.getCodDpto());

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
