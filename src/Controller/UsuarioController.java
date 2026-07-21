/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;
import java.util.ArrayList;

public class UsuarioController {

    private final UsuarioDAO dao = new UsuarioDAO();

    public void salvar(Usuario usuario) {
        dao.salvar(usuario);
    }

    public void atualizar(Usuario usuario) {
        dao.atualizar(usuario); // Chama o método de UPDATE do DAO
    }

    public ArrayList<Usuario> listar() {
        return dao.listar();
    }
    
    public Usuario autenticar(String login, String senha) {
        return dao.autenticar(login, senha);
    }

    public void excluir(int id) {
        dao.excluir(id);
    }
}
