/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.EnderecoDAO;
import Model.Endereco;
import java.util.ArrayList;

public class EnderecoController {

    private EnderecoDAO dao = new EnderecoDAO();

    public void salvar(Endereco endereco) {
        dao.salvar(endereco);
    }

    public ArrayList<Endereco> listar() {
        return dao.listar();
    }
}
