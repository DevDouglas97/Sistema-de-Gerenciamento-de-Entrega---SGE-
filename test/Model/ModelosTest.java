package Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import org.junit.Test;

public class ModelosTest {

    @Test
    public void expedicaoTemValoresPadraoEItensVazios() {
        Expedicao expedicao = new Expedicao();

        assertEquals(0, expedicao.getId());
        assertEquals(0, expedicao.getClienteId());
        assertEquals(0, expedicao.getMotoristaId());
        assertEquals("Aguardando", expedicao.getStatus());
        assertNotNull(expedicao.getItensPedido());
        assertTrue(expedicao.getItensPedido().isEmpty());
    }

    @Test
    public void expedicaoGuardaIdsRelacionados() {
        Expedicao expedicao = new Expedicao();
        expedicao.setId(10);
        expedicao.setClienteId(2);
        expedicao.setMotoristaId(3);
        expedicao.setCliente("Empresa X");
        expedicao.setMotorista("João");

        assertEquals(10, expedicao.getId());
        assertEquals(2, expedicao.getClienteId());
        assertEquals(3, expedicao.getMotoristaId());
        assertEquals("Empresa X", expedicao.getCliente());
        assertEquals("João", expedicao.getMotorista());
    }

    @Test
    public void adicionarItemMantemOrdem() {
        Expedicao expedicao = new Expedicao();
        Produtos primeiro = new Produtos();
        primeiro.setNomeProduto("A");
        Produtos segundo = new Produtos();
        segundo.setNomeProduto("B");

        expedicao.adicionarItem(primeiro);
        expedicao.adicionarItem(segundo);

        assertEquals(2, expedicao.getItensPedido().size());
        assertEquals("A", expedicao.getItensPedido().get(0).getNomeProduto());
        assertEquals("B", expedicao.getItensPedido().get(1).getNomeProduto());
    }

    @Test
    public void construtorCompletoDeExpedicao() {
        ArrayList<Produtos> itens = new ArrayList<>();
        Expedicao expedicao = new Expedicao(1, "Cliente", "Motorista", "Separado", itens);

        assertEquals(1, expedicao.getId());
        assertEquals("Cliente", expedicao.getCliente());
        assertEquals("Motorista", expedicao.getMotorista());
        assertEquals("Separado", expedicao.getStatus());
        assertEquals(itens, expedicao.getItensPedido());
    }

    @Test
    public void enderecoGuardaClienteId() {
        Endereco endereco = new Endereco();
        assertEquals(0, endereco.getClienteId());

        endereco.setClienteId(7);
        endereco.setCliente("Empresa Y");
        endereco.setDescricao("Sede");

        assertEquals(7, endereco.getClienteId());
        assertEquals("Empresa Y", endereco.getCliente());
        assertEquals("Sede", endereco.getDescricao());
    }

    @Test
    public void produtoGuardaClienteIdEProdutoId() {
        Produtos produto = new Produtos();
        assertEquals(0, produto.getClienteId());

        produto.setId(99);
        produto.setClienteId(4);
        produto.setNomeProduto("Caixa");
        produto.setQuantidade(10);
        produto.setUnidade("CX");

        assertEquals(99, produto.getId());
        assertEquals(4, produto.getClienteId());
        assertEquals("Caixa", produto.getNomeProduto());
        assertEquals(10, produto.getQuantidade());
        assertEquals("CX", produto.getUnidade());
    }

    @Test
    public void rotaGuardaIdsEDisplay() {
        Rotas rota = new Rotas();
        assertEquals(0, rota.getExpedicaoId());
        assertEquals(0, rota.getEnderecoId());
        assertEquals("Pendente", rota.getStatus());

        rota.setId(5);
        rota.setExpedicaoId(12);
        rota.setEnderecoId(8);
        rota.setPedidoDisplay("#12");

        assertEquals(5, rota.getId());
        assertEquals(12, rota.getExpedicaoId());
        assertEquals(8, rota.getEnderecoId());
        assertEquals("#12", rota.getPedidoDisplay());
    }
}
