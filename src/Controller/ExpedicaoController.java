/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ExpedicaoDAO;
import Model.Expedicao;
import java.util.ArrayList;

public class ExpedicaoController {

    private final ExpedicaoDAO dao = new ExpedicaoDAO();

    /**
     * Decisão inteligente de persistência:
     * - Se o ID for maior que 0, chama o atualizar (UPDATE).
     * - Se o ID for 0 ou nulo, salva um novo registro (INSERT).
     */
    public void salvar(Expedicao expedicao) {
        if (expedicao.getId() > 0) {
            dao.atualizar(expedicao);
        } else {
            dao.salvar(expedicao);
        }
    }

    /**
     * Retorna a lista completa de expedições cadastradas.
     */
    public ArrayList<Expedicao> listar() {
        return dao.listar();
    }

    /**
     * Busca uma expedição específica no banco de dados pelo ID.
     */
    public Expedicao buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    /**
     * Exclui a expedição e seus itens vinculados pelo ID.
     */
    public void excluir(int id) {
        dao.excluir(id);
    }
}