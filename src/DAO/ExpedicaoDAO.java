/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Expedicao;
import java.util.ArrayList;

public class ExpedicaoDAO {

    // Lista estática compartilhada em memória contendo todos os pedidos gravados
    private static ArrayList<Expedicao> listaExpedicoes = new ArrayList<>();

    public void salvar(Expedicao expedicao) {
        listaExpedicoes.add(expedicao);
    }

    public ArrayList<Expedicao> listar() {
        return listaExpedicoes;
    }
}

