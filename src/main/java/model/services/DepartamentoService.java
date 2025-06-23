/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.services;

import java.util.List;
import model.DB.DB;
import model.classes.Departamento;
import model.dao.DepartamentoDao;

/**
 *
 * @author herrmann
 */
public class DepartamentoService {

    private DepartamentoDao dao = new DepartamentoDao(DB.getConnection());

    public List<Departamento> getAll() {
        return dao.getAll();
    }

    public boolean salvarOuAtualizar(Departamento dpto) {
        if (dpto.getCodDpto() <= 0) {
            return dao.inserir(dpto);
        } else {
            return dao.editar(dpto);
        }
    }

    public boolean excluir(Departamento dpto) {
        return dao.excluir(dpto);
    }
}
