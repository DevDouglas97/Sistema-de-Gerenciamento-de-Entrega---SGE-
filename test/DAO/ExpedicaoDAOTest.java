package DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import Model.Expedicao;
import Model.Produtos;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExpedicaoDAOTest {

    private ExpedicaoDAO dao;

    @BeforeClass
    public static void setUpClass() {
        ConexaoTesteUtil.configurar();
    }

    @Before
    public void setUp() {
        ConexaoTesteUtil.recriarSchema();
        dao = new ExpedicaoDAO();
    }

    @Test
    public void salvaComIdsRelacionadosEItens() {
        int clienteId = inserirCliente("Cliente Expedição");
        int motoristaId = inserirMotorista("Motorista Um");
        int produtoId = inserirProduto(clienteId, "Caixa", 100, "CX");

        Expedicao expedicao = new Expedicao();
        expedicao.setClienteId(clienteId);
        expedicao.setCliente("Cliente Expedição");
        expedicao.setMotoristaId(motoristaId);
        expedicao.setMotorista("Motorista Um");
        expedicao.setStatus("Aguardando");
        expedicao.adicionarItem(item(produtoId, "Caixa", 3, "CX"));

        dao.salvar(expedicao);

        ArrayList<Expedicao> lista = dao.listar();
        assertEquals(1, lista.size());

        Expedicao salva = lista.get(0);
        assertEquals(clienteId, salva.getClienteId());
        assertEquals(motoristaId, salva.getMotoristaId());
        assertEquals("Cliente Expedição", salva.getCliente());
        assertEquals("Motorista Um", salva.getMotorista());
        assertEquals(1, salva.getItensPedido().size());
        assertEquals(produtoId, salva.getItensPedido().get(0).getId());
        assertEquals(3, salva.getItensPedido().get(0).getQuantidade());
    }

    @Test
    public void salvaUsandoFallbackPorNomes() {
        int clienteId = inserirCliente("Cliente Fallback");
        int motoristaId = inserirMotorista("Motorista Fallback");
        int produtoId = inserirProduto(clienteId, "Saco", 20, "PCT");

        Expedicao expedicao = new Expedicao();
        expedicao.setCliente("Cliente Fallback");
        expedicao.setMotorista("Motorista Fallback");
        expedicao.setStatus("Aguardando");
        expedicao.adicionarItem(item(0, "Saco", 2, "PCT"));

        dao.salvar(expedicao);

        Expedicao salva = dao.listar().get(0);
        assertEquals(clienteId, salva.getClienteId());
        assertEquals(motoristaId, salva.getMotoristaId());
        assertEquals(produtoId, salva.getItensPedido().get(0).getId());
    }

    @Test
    public void buscaPorIdCarregaItens() {
        int clienteId = inserirCliente("Cliente Busca");
        int motoristaId = inserirMotorista("Motorista Busca");
        int produtoId = inserirProduto(clienteId, "Produto Busca", 10, "UN");

        Expedicao expedicao = new Expedicao();
        expedicao.setClienteId(clienteId);
        expedicao.setCliente("Cliente Busca");
        expedicao.setMotoristaId(motoristaId);
        expedicao.setMotorista("Motorista Busca");
        expedicao.setStatus("Aguardando");
        expedicao.adicionarItem(item(produtoId, "Produto Busca", 4, "UN"));
        dao.salvar(expedicao);

        int id = dao.listar().get(0).getId();
        Expedicao encontrada = dao.buscarPorId(id);

        assertNotNull(encontrada);
        assertEquals(id, encontrada.getId());
        assertEquals(1, encontrada.getItensPedido().size());
    }

    @Test
    public void atualizaStatusERefazItens() {
        int clienteId = inserirCliente("Cliente Update");
        int motoristaId = inserirMotorista("Motorista Update");
        int produto1 = inserirProduto(clienteId, "Produto 1", 10, "UN");
        int produto2 = inserirProduto(clienteId, "Produto 2", 10, "UN");

        Expedicao expedicao = new Expedicao();
        expedicao.setClienteId(clienteId);
        expedicao.setCliente("Cliente Update");
        expedicao.setMotoristaId(motoristaId);
        expedicao.setMotorista("Motorista Update");
        expedicao.setStatus("Aguardando");
        expedicao.adicionarItem(item(produto1, "Produto 1", 1, "UN"));
        dao.salvar(expedicao);

        Expedicao salva = dao.listar().get(0);
        salva.setStatus("Separado");
        salva.getItensPedido().clear();
        salva.adicionarItem(item(produto2, "Produto 2", 7, "UN"));

        dao.atualizar(salva);

        Expedicao atualizada = dao.buscarPorId(salva.getId());
        assertNotNull(atualizada);
        assertEquals("Separado", atualizada.getStatus());
        assertEquals(1, atualizada.getItensPedido().size());
        assertEquals(produto2, atualizada.getItensPedido().get(0).getId());
        assertEquals(7, atualizada.getItensPedido().get(0).getQuantidade());
    }

    @Test
    public void buscaInexistenteRetornaNull() {
        assertNull(dao.buscarPorId(404));
    }

    @Test
    public void excluiExpedicaoEItens() {
        int clienteId = inserirCliente("Cliente Delete");
        int motoristaId = inserirMotorista("Motorista Delete");
        int produtoId = inserirProduto(clienteId, "Produto Delete", 1, "UN");

        Expedicao expedicao = new Expedicao();
        expedicao.setClienteId(clienteId);
        expedicao.setCliente("Cliente Delete");
        expedicao.setMotoristaId(motoristaId);
        expedicao.setMotorista("Motorista Delete");
        expedicao.setStatus("Aguardando");
        expedicao.adicionarItem(item(produtoId, "Produto Delete", 1, "UN"));
        dao.salvar(expedicao);

        int id = dao.listar().get(0).getId();
        dao.excluir(id);

        assertEquals(0, dao.listar().size());
        assertEquals(0, ConexaoTesteUtil.contar("itens_expedicao"));
    }

    private int inserirCliente(String nome) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO clientes (nome_empresa, cnpj, telefone, email) VALUES (?, ?, ?, ?)",
                nome, "000", "999", "teste@teste.com");
    }

    private int inserirMotorista(String nome) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO usuarios (nome, cpf, login, senha, perfil, ativo) VALUES (?, ?, ?, ?, ?, ?)",
                nome, "000", nome.toLowerCase().replace(" ", ""), "hash", "Motorista", true);
    }

    private int inserirProduto(int clienteId, String nome, int quantidade, String unidade) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO produtos (cliente_id, nome_produto, quantidade, unidade, categoria) "
                + "VALUES (?, ?, ?, ?, ?)",
                clienteId, nome, quantidade, unidade, "Geral");
    }

    private Produtos item(int produtoId, String nome, int quantidade, String unidade) {
        Produtos produto = new Produtos();
        produto.setId(produtoId);
        produto.setNomeProduto(nome);
        produto.setQuantidade(quantidade);
        produto.setUnidade(unidade);
        return produto;
    }
}
