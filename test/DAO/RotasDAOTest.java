package DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import Model.Rotas;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RotasDAOTest {

    private RotasDAO dao;

    @BeforeClass
    public static void setUpClass() {
        ConexaoTesteUtil.configurar();
    }

    @Before
    public void setUp() {
        ConexaoTesteUtil.recriarSchema();
        dao = new RotasDAO();
    }

    @Test
    public void salvaRotaComIdsRelacionados() {
        int clienteId = inserirCliente("Cliente Rota");
        int enderecoId = inserirEndereco(clienteId, "Rua Rota, 1");
        int expedicaoId = inserirExpedicao(clienteId, "Aguardando");

        Rotas rota = new Rotas();
        rota.setExpedicaoId(expedicaoId);
        rota.setEnderecoId(enderecoId);
        rota.setEnderecoEntrega("Rua Rota, 1");
        rota.setStatus("Pendente");

        dao.salvar(rota);

        ArrayList<Rotas> lista = dao.listar();
        assertEquals(1, lista.size());

        Rotas salva = lista.get(0);
        assertEquals(expedicaoId, salva.getExpedicaoId());
        assertEquals(enderecoId, salva.getEnderecoId());
        assertEquals("#" + expedicaoId, salva.getPedidoDisplay());
        assertEquals("Cliente Rota", salva.getCliente());
        assertEquals("Rua Rota, 1", salva.getEnderecoEntrega());
        assertEquals("Pendente", salva.getStatus());
    }

    @Test
    public void salvaUsandoFallbackDeTexto() {
        int clienteId = inserirCliente("Cliente Fallback Rota");
        int enderecoId = inserirEndereco(clienteId, "Rua Fallback, 2");
        int expedicaoId = inserirExpedicao(clienteId, "Aguardando");

        Rotas rota = new Rotas();
        rota.setPedidoDisplay("Pedido #" + expedicaoId);
        rota.setEnderecoEntrega("Rua Fallback, 2");
        rota.setStatus("Pendente");

        dao.salvar(rota);

        Rotas salva = dao.listar().get(0);
        assertEquals(expedicaoId, salva.getExpedicaoId());
        assertEquals(enderecoId, salva.getEnderecoId());
    }

    @Test
    public void atualizaStatusEEndereco() {
        int clienteId = inserirCliente("Cliente Update Rota");
        int endereco1 = inserirEndereco(clienteId, "Rua Um, 10");
        int endereco2 = inserirEndereco(clienteId, "Rua Dois, 20");
        int expedicaoId = inserirExpedicao(clienteId, "Aguardando");

        Rotas rota = new Rotas();
        rota.setExpedicaoId(expedicaoId);
        rota.setEnderecoId(endereco1);
        rota.setEnderecoEntrega("Rua Um, 10");
        rota.setStatus("Pendente");
        dao.salvar(rota);

        Rotas salva = dao.listar().get(0);
        salva.setStatus("Entregue");
        salva.setEnderecoId(endereco2);
        salva.setEnderecoEntrega("Rua Dois, 20");

        dao.atualizar(salva);

        Rotas atualizada = dao.buscarPorId(salva.getId());
        assertNotNull(atualizada);
        assertEquals("Entregue", atualizada.getStatus());
        assertEquals(endereco2, atualizada.getEnderecoId());
        assertEquals("Rua Dois, 20", atualizada.getEnderecoEntrega());
    }

    @Test
    public void buscaInexistenteRetornaNull() {
        assertNull(dao.buscarPorId(999));
    }

    @Test
    public void excluiRota() {
        int clienteId = inserirCliente("Cliente Delete Rota");
        int enderecoId = inserirEndereco(clienteId, "Rua Delete, 30");
        int expedicaoId = inserirExpedicao(clienteId, "Aguardando");

        Rotas rota = new Rotas();
        rota.setExpedicaoId(expedicaoId);
        rota.setEnderecoId(enderecoId);
        rota.setEnderecoEntrega("Rua Delete, 30");
        rota.setStatus("Pendente");
        dao.salvar(rota);

        int id = dao.listar().get(0).getId();
        dao.excluir(id);

        assertEquals(0, dao.listar().size());
    }

    @Test
    public void resolverExpedicaoUsaIdQuandoInformado() {
        Rotas rota = new Rotas();
        rota.setExpedicaoId(77);
        rota.setPedidoDisplay("#1");

        assertEquals(77, dao.resolverExpedicaoId(rota));
    }

    @Test
    public void resolverExpedicaoExtraiPrimeiroNumeroDoDisplay() {
        Rotas rota = new Rotas();
        rota.setPedidoDisplay("Pedido #42 - Cliente");

        assertEquals(42, dao.resolverExpedicaoId(rota));
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolverExpedicaoSemIdNemNumeroLanca() {
        Rotas rota = new Rotas();
        rota.setPedidoDisplay("Pedido sem número");

        dao.resolverExpedicaoId(rota);
    }

    private int inserirCliente(String nome) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO clientes (nome_empresa, cnpj, telefone, email) VALUES (?, ?, ?, ?)",
                nome, "000", "999", "teste@teste.com");
    }

    private int inserirEndereco(int clienteId, String logradouro) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO enderecos (cliente_id, descricao, endereco, bairro, cidade_uf) "
                + "VALUES (?, ?, ?, ?, ?)",
                clienteId, "Sede", logradouro, "Centro", "Porto Alegre/RS");
    }

    private int inserirExpedicao(int clienteId, String status) {
        return ConexaoTesteUtil.inserir(
                "INSERT INTO expedicao (cliente_id, motorista_id, status) VALUES (?, ?, ?)",
                clienteId, null, status);
    }
}
