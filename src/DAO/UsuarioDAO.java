/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Usuario;
import java.util.ArrayList;

public class UsuarioDAO {

    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    public void salvar(Usuario usuario){
        usuarios.add(usuario);
    }

    public ArrayList<Usuario> listar(){
        return usuarios;
    }

}