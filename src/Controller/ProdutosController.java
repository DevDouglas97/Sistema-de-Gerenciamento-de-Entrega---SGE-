/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ProdutosDAO;
import Model.Produtos;
import java.util.ArrayList;

public class ProdutosController {

    private ProdutosDAO dao = new ProdutosDAO();

    public void salvar(Produtos produto) {
        dao.salvar(produto);
    }

    public ArrayList<Produtos> listar() {
        return dao.listar();
    }
}

