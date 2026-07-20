/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Rotas;
import java.util.ArrayList;

public class RotasDAO {

    // Lista estática compartilhada em memória RAM
    private static ArrayList<Rotas> listaRotas = new ArrayList<>();

    public void salvar(Rotas rota) {
        listaRotas.add(rota);
    }

    public ArrayList<Rotas> listar() {
        return listaRotas;
    }
}

