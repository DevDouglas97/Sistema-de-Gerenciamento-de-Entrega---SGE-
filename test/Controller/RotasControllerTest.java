package Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import DAO.RotasDAO;
import Model.Rotas;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class RotasControllerTest {

    private DaoEspiao dao;
    private RotasController controller;

    @Before
    public void setUp() {
        dao = new DaoEspiao();
        controller = new RotasController(dao);
    }

    @Test
    public void salvaRotaValidaUsandoIds() {
        Rotas rota = new Rotas();
        rota.setExpedicaoId(10);
        rota.setEnderecoId(20);
        rota.setStatus("Pendente");

        controller.salvar(rota);

        assertTrue(dao.salvo);
        assertEquals(rota, dao.ultimaRota);
    }

    @Test
    public void salvaRotaValidaUsandoTextosLegados() {
        Rotas rota = new Rotas();
        rota.setPedidoDisplay("#10");
        rota.setEnderecoEntrega("Rua A, 100");
        rota.setStatus("Pendente");

        controller.salvar(rota);

        assertTrue(dao.salvo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejeitaRotaNula() {
        controller.salvar(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejeitaRotaSemPedido() {
        Rotas rota = new Rotas();
        rota.setEnderecoId(20);
        rota.setStatus("Pendente");

        controller.salvar(rota);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejeitaRotaSemEndereco() {
        Rotas rota = new Rotas();
        rota.setExpedicaoId(10);
        rota.setStatus("Pendente");

        controller.salvar(rota);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejeitaRotaSemStatus() {
        Rotas rota = new Rotas();
        rota.setExpedicaoId(10);
        rota.setEnderecoId(20);
        rota.setStatus("   ");

        controller.salvar(rota);
    }

    @Test
    public void naoChamaDaoQuandoValidacaoFalha() {
        try {
            controller.salvar(new Rotas());
        } catch (IllegalArgumentException esperado) {
            // ok
        }
        assertFalse(dao.salvo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void atualizarExigeIdValido() {
        Rotas rota = new Rotas();
        rota.setExpedicaoId(10);
        rota.setEnderecoId(20);
        rota.setStatus("Pendente");

        controller.atualizar(rota);
    }

    @Test
    public void atualizarValidoChamaDao() {
        Rotas rota = new Rotas();
        rota.setId(1);
        rota.setExpedicaoId(10);
        rota.setEnderecoId(20);
        rota.setStatus("Entregue");

        controller.atualizar(rota);

        assertTrue(dao.atualizado);
        assertEquals("Entregue", dao.ultimaRota.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void buscarPorIdInvalidoLanca() {
        controller.buscarPorId(0);
    }

    @Test
    public void buscarPorIdDelegaAoDao() {
        assertNull(controller.buscarPorId(5));
        assertTrue(dao.buscou);
    }

    @Test(expected = IllegalArgumentException.class)
    public void excluirIdInvalidoLanca() {
        controller.excluir(-1);
    }

    @Test
    public void excluirValidoDelegaAoDao() {
        controller.excluir(5);
        assertTrue(dao.excluiu);
    }

    @Test
    public void listarDelegaAoDao() {
        assertTrue(controller.listar().isEmpty());
        assertTrue(dao.listou);
    }

    private static class DaoEspiao extends RotasDAO {
        boolean salvo;
        boolean atualizado;
        boolean excluiu;
        boolean buscou;
        boolean listou;
        Rotas ultimaRota;

        @Override
        public void salvar(Rotas rota) {
            salvo = true;
            ultimaRota = rota;
        }

        @Override
        public void atualizar(Rotas rota) {
            atualizado = true;
            ultimaRota = rota;
        }

        @Override
        public void excluir(int id) {
            excluiu = true;
        }

        @Override
        public Rotas buscarPorId(int id) {
            buscou = true;
            return null;
        }

        @Override
        public ArrayList<Rotas> listar() {
            listou = true;
            return new ArrayList<>();
        }
    }
}
