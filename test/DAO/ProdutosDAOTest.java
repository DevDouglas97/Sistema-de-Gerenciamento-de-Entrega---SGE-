package DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import Model.Produtos;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProdutosDAOTest {

    private ProdutosDAO dao;

    @BeforeClass
    public static void setUpClass() {
        ConexaoTesteUtil.configurar();
    }

    @Before
    public void setUp() {
        ConexaoTesteUtil.recriarSchema();
        dao = new ProdutosDAO();
    }

    @Test
    public void salvaVinculandoPeloClienteId() {
        int clienteId = inserirCliente("Empresa Produtos");

        Produtos produto = novoProduto(clienteId, "Empresa Produtos", "Caixa", 50, "CX");
        dao.salvar(produto);

        ArrayList<Produtos> lista = dao.listar();
        assertEquals(1, lista.size());
        assertEquals(clienteId, lista.get(0).getClienteId());
        assertEquals("Empresa Produtos", lista.get(0).getCliente());
        assertEquals("Caixa", lista.get(0).getNomeProduto());
        assertEquals(50, lista.get(0).getQuantidade());
    }

    @Test
    public void salvaUsandoFallbackPorNome() {
        int clienteId = inserirCliente("Empresa Fallback");

        Produtos produto = novoProduto(0, "Empresa Fallback", "Saco", 10, "PCT");
        dao.salvar(produto);

        assertEquals(clienteId, dao.listar().get(0).getClienteId());
    }

    @Test
    public void atualizaProduto() {
        int clienteId = inserirCliente("Empresa Update");
        dao.salvar(novoProduto(clienteId, "Empresa Update", "Antigo", 5, "UN"));

        Produtos salvo = dao.listar().get(0);
        salvo.setNomeProduto("Novo");
        salvo.setQuantidade(99);
        salvo.setUnidade("KG");
        salvo.setCategoria("Alimentos");

        dao.atualizar(salvo);

        Produtos atualizado = dao.buscarPorId(salvo.getId());
        assertNotNull(atualizado);
        assertEquals("Novo", atualizado.getNomeProduto());
        assertEquals(99, atualizado.getQuantidade());
        assertEquals("KG", atualizado.getUnidade());
        assertEquals("Alimentos", atualizado.getCategoria());
    }

    @Test
    public void buscaInexistenteRetornaNull() {
        assertNull(dao.buscarPorId(12345));
    }

    @Test
    public void excluiProduto() {
        int clienteId = inserirCliente("Empresa Delete");
        dao.salvar(novoProduto(clienteId, "Empresa Delete", "X", 1, "UN"));

        int id = dao.listar().get(0).getId();
        dao.excluir(id);

        assertEquals(0, dao.listar().size());
    }

    private int inserirCliente(String nome) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO clientes (nome_empresa, cnpj, telefone, email) VALUES (?, ?, ?, ?)",
                nome, "000", "999", "teste@teste.com");
    }

    private Produtos novoProduto(int clienteId, String cliente, String nome, int quantidade, String unidade) {
        Produtos produto = new Produtos();
        produto.setClienteId(clienteId);
        produto.setCliente(cliente);
        produto.setNomeProduto(nome);
        produto.setQuantidade(quantidade);
        produto.setUnidade(unidade);
        produto.setCategoria("Geral");
        return produto;
    }
}
