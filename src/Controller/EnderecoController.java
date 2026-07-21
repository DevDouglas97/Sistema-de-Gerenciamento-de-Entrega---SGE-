/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.EnderecoDAO;
import Model.Endereco;
import java.util.ArrayList;

/**
 * Controller responsável por intermediar a comunicação entre a interface visual
 * (Panels/Telas) e a camada de acesso a dados (EnderecoDAO).
 */
public class EnderecoController {

    private final EnderecoDAO dao = new EnderecoDAO();

    /**
     * Identifica automaticamente se é uma inclusão ou edição.
     * Se o endereço já possuir ID, atualiza no banco; caso contrário, insere um novo.
     */
    public void salvar(Endereco endereco) {
        if (endereco.getId() > 0) {
            dao.atualizar(endereco);
        } else {
            dao.salvar(endereco);
        }
    }

    /**
     * Retorna a lista completa de endereços cadastrados.
     */
    public ArrayList<Endereco> listar() {
        return dao.listar();
    }

    /**
     * Busca um endereço específico pelo ID para preencher a tela de edição.
     */
    public Endereco buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    /**
     * Remove o endereço permanentemente do banco de dados pelo ID.
     */
    public void excluir(int id) {
        dao.excluir(id);
    }
}