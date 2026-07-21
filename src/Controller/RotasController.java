/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.RotasDAO;
import Model.Rotas;
import java.util.ArrayList;

public class RotasController {

    private final RotasDAO dao = new RotasDAO();

    /**
     * Valida os dados da rota e salva no banco de dados.
     */
    public void salvar(Rotas rota) {
        validarRota(rota);
        dao.salvar(rota);
    }

    /**
     * Valida e atualiza os dados de uma rota existente.
     */
    public void atualizar(Rotas rota) {
        if (rota.getId() <= 0) {
            throw new IllegalArgumentException("ID da rota inválido para atualização.");
        }
        validarRota(rota);
        dao.atualizar(rota);
    }

    /**
     * Retorna todas as rotas cadastradas.
     */
    public ArrayList<Rotas> listar() {
        return dao.listar();
    }

    /**
     * Busca uma rota específica pelo seu ID.
     */
    public Rotas buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca.");
        }
        return dao.buscarPorId(id);
    }

    /**
     * Exclui uma rota pelo seu ID.
     */
    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido para exclusão.");
        }
        dao.excluir(id);
    }

    /**
     * Método privado para centralizar a validação dos campos obrigatórios da rota.
     */
    private void validarRota(Rotas rota) {
        if (rota == null) {
            throw new IllegalArgumentException("A rota não pode ser nula.");
        }
        if (rota.getPedidoDisplay() == null || rota.getPedidoDisplay().trim().isEmpty()) {
            throw new IllegalArgumentException("O pedido vinculado é obrigatório.");
        }
        if (rota.getEnderecoEntrega() == null || rota.getEnderecoEntrega().trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço de entrega é obrigatório.");
        }
        if (rota.getStatus() == null || rota.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("O status da rota é obrigatório.");
        }
    }
}
