package Controller;

import DAO.ProdutosDAO;
import Model.Produtos;
import java.util.ArrayList;

public class ProdutosController {

    private final ProdutosDAO dao = new ProdutosDAO();

    /**
     * Identifica se é um novo registro ou edição:
     * - Se o id for maior que 0, chama o UPDATE (atualizar).
     * - Se o id for 0, chama o INSERT (salvar).
     */
    public void salvar(Produtos produto) {
        if (produto.getId() > 0) {
            dao.atualizar(produto);
        } else {
            dao.salvar(produto);
        }
    }

    /**
     * Retorna a lista de produtos gravados no banco de dados.
     */
    public ArrayList<Produtos> listar() {
        return dao.listar();
    }

    /**
     * Busca um produto específico pelo ID.
     */
    public Produtos buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    /**
     * Remove um produto do banco de dados pelo ID.
     */
    public void excluir(int id) {
        dao.excluir(id);
    }
}