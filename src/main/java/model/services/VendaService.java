package model.services;

import java.util.List;
import model.DB.DB;
import model.classes.Venda;
import model.dao.VendaDao;

public class VendaService {
    private VendaDao dao = new VendaDao(DB.getConnection());

    public List<Venda> getAll() {
        return dao.getAll();
    }

    // ✅ NOVO: Para usar nos relatórios
    public List<Venda> getVendasComProdutos() {
        return dao.getVendasComProdutos();
    }
}
