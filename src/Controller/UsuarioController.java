/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;
import java.util.ArrayList;

public class UsuarioController {

    UsuarioDAO dao = new UsuarioDAO();

    public void salvar(Usuario usuario){
        dao.salvar(usuario);
    }

    public ArrayList<Usuario> listar(){
        return dao.listar();
    }

}