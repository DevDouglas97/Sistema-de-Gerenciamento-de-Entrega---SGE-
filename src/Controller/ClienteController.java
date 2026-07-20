/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ClienteDAO;
import Model.Cliente;
import java.util.ArrayList;

public class ClienteController {

    private ClienteDAO dao = new ClienteDAO();

    public void salvar(Cliente cliente) {
        dao.salvar(cliente);
    }

    public ArrayList<Cliente> listar() {
        return dao.listar();
    }
}

