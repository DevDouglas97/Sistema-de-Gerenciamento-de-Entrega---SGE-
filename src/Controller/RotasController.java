/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.RotasDAO;
import Model.Rotas;
import java.util.ArrayList;

public class RotasController {

    private RotasDAO dao = new RotasDAO();

    public void salvar(Rotas rota) {
        dao.salvar(rota);
    }

    public ArrayList<Rotas> listar() {
        return dao.listar();
    }
}

