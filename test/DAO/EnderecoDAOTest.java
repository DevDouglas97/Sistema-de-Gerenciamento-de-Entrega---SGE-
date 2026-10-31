package DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import Model.Endereco;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EnderecoDAOTest {

    private EnderecoDAO dao;

    @BeforeClass
    public static void setUpClass() {
        ConexaoTesteUtil.configurar();
    }

    @Before
    public void setUp() {
        ConexaoTesteUtil.recriarSchema();
        dao = new EnderecoDAO();
    }

    @Test
    public void salvaVinculandoPeloClienteId() {
        int clienteId = inserirCliente("Empresa A");

        Endereco endereco = novoEndereco(clienteId, "Empresa A", "Sede", "Rua A, 10");
        dao.salvar(endereco);

        ArrayList<Endereco> lista = dao.listar();
        assertEquals(1, lista.size());
        assertEquals(clienteId, lista.get(0).getClienteId());
        assertEquals("Empresa A", lista.get(0).getCliente());
        assertEquals("Rua A, 10", lista.get(0).getEndereco());
    }

    @Test
    public void salvaUsandoFallbackPorNome() {
        int clienteId = inserirCliente("Empresa B");

        Endereco endereco = novoEndereco(0, "Empresa B", "Filial", "Rua B, 20");
        dao.salvar(endereco);

        ArrayList<Endereco> lista = dao.listar();
        assertEquals(1, lista.size());
        assertEquals(clienteId, lista.get(0).getClienteId());
    }

    @Test
    public void atualizaDadosDoEndereco() {
        int clienteId = inserirCliente("Empresa C");
        Endereco endereco = novoEndereco(clienteId, "Empresa C", "Sede", "Rua C, 30");
        dao.salvar(endereco);

        Endereco salvo = dao.listar().get(0);
        salvo.setDescricao("Depósito");
        salvo.setEndereco("Rua C, 300");
        salvo.setBairro("Industrial");
        salvo.setCidadeUf("Canoas/RS");

        dao.atualizar(salvo);

        Endereco atualizado = dao.buscarPorId(salvo.getId());
        assertNotNull(atualizado);
        assertEquals("Depósito", atualizado.getDescricao());
        assertEquals("Rua C, 300", atualizado.getEndereco());
        assertEquals("Industrial", atualizado.getBairro());
        assertEquals("Canoas/RS", atualizado.getCidadeUf());
    }

    @Test
    public void buscaInexistenteRetornaNull() {
        assertNull(dao.buscarPorId(999));
    }

    @Test
    public void excluiEndereco() {
        int clienteId = inserirCliente("Empresa D");
        dao.salvar(novoEndereco(clienteId, "Empresa D", "Sede", "Rua D, 40"));

        int id = dao.listar().get(0).getId();
        dao.excluir(id);

        assertEquals(0, dao.listar().size());
        assertNull(dao.buscarPorId(id));
    }

    private int inserirCliente(String nome) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO clientes (nome_empresa, cnpj, telefone, email) VALUES (?, ?, ?, ?)",
                nome, "000", "999", nome.toLowerCase().replace(" ", "") + "@teste.com");
    }

    private Endereco novoEndereco(int clienteId, String cliente, String descricao, String logradouro) {
        Endereco endereco = new Endereco();
        endereco.setClienteId(clienteId);
        endereco.setCliente(cliente);
        endereco.setDescricao(descricao);
        endereco.setEndereco(logradouro);
        endereco.setBairro("Centro");
        endereco.setCidadeUf("Porto Alegre/RS");
        return endereco;
    }
}
