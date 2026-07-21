/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ClienteDAO;
import Model.Cliente;
import java.util.ArrayList;

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO();

    public void salvar(Cliente cliente) {
        if (cliente.getId() > 0) {
            dao.atualizar(cliente);
        } else {
            dao.salvar(cliente);
        }
    }

    public ArrayList<Cliente> listar() {
        return dao.listar();
    }

    public Cliente buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public void atualizar(Cliente cliente) {
        dao.atualizar(cliente);
    }

    public void excluir(int id) {
        dao.excluir(id);
    }
}

