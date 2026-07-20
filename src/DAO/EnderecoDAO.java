/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Endereco;
import java.util.ArrayList;

public class EnderecoDAO {

    // Lista estática compartilhada em memória
    private static ArrayList<Endereco> enderecos = new ArrayList<>();

    public void salvar(Endereco endereco) {
        enderecos.add(endereco);
    }

    public ArrayList<Endereco> listar() {
        return enderecos;
    }
}

