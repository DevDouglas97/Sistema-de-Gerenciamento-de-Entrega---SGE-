/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ExpedicaoDAO;
import Model.Expedicao;
import java.util.ArrayList;

public class ExpedicaoController {

    private ExpedicaoDAO dao = new ExpedicaoDAO();

    public void salvar(Expedicao expedicao) {
        dao.salvar(expedicao);
    }

    public ArrayList<Expedicao> listar() {
        return dao.listar();
    }
}
