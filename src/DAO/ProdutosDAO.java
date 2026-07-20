/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Produtos;
import java.util.ArrayList;

public class ProdutosDAO {

    // Lista estática centralizada no plural
    private static ArrayList<Produtos> listaProdutos = new ArrayList<>();

    public void salvar(Produtos produto) {
        listaProdutos.add(produto);
    }

    public ArrayList<Produtos> listar() {
        return listaProdutos;
    }
}

