/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.services;

import java.util.List;
import model.DB.DB;
import model.classes.Vendedor;
import model.dao.VendedorDao;

/**
 *
 * @author herrmann
 */
public class VendedorService {

    private VendedorDao dao = new VendedorDao(DB.getConnection());

    public List<Vendedor> getAll() {
        return dao.getAll();
    }

    public boolean salvarOuAtualizar(Vendedor vend) {
        if (vend.getCodVendedor()<= 0) {
            return dao.inserir(vend);
        } else {
            return dao.editar(vend);
        }
    }

    public boolean excluir(Vendedor vend) {
        return dao.excluir(vend);
    }
}
