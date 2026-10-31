package Util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PermissaoUtilTest {

    private static final PermissaoUtil.Modulo[] TODOS = PermissaoUtil.Modulo.values();

    @Test
    public void gerenteAcessaTodosOsModulos() {
        for (PermissaoUtil.Modulo modulo : TODOS) {
            assertTrue("Gerente deveria acessar " + modulo,
                    PermissaoUtil.podeAcessar("Gerente", modulo));
        }
    }

    @Test
    public void administradorAcessaTodosOsModulos() {
        for (PermissaoUtil.Modulo modulo : TODOS) {
            assertTrue(PermissaoUtil.podeAcessar("Administrador", modulo));
            assertTrue(PermissaoUtil.podeAcessar("admin", modulo));
        }
    }

    @Test
    public void motoristaAcessaSomenteOperacao() {
        assertTrue(PermissaoUtil.podeAcessar("Motorista", PermissaoUtil.Modulo.DASHBOARD));
        assertTrue(PermissaoUtil.podeAcessar("Motorista", PermissaoUtil.Modulo.ROTAS));
        assertTrue(PermissaoUtil.podeAcessar("Motorista", PermissaoUtil.Modulo.EXPEDICAO));

        assertFalse(PermissaoUtil.podeAcessar("Motorista", PermissaoUtil.Modulo.USUARIOS));
        assertFalse(PermissaoUtil.podeAcessar("Motorista", PermissaoUtil.Modulo.CLIENTES));
        assertFalse(PermissaoUtil.podeAcessar("Motorista", PermissaoUtil.Modulo.PRODUTOS));
        assertFalse(PermissaoUtil.podeAcessar("Motorista", PermissaoUtil.Modulo.ENDERECOS));
    }

    @Test
    public void auxiliarEExpedidorNaoGerenciamUsuarios() {
        assertFalse(PermissaoUtil.podeAcessar("Auxiliar Logistico", PermissaoUtil.Modulo.USUARIOS));
        assertFalse(PermissaoUtil.podeAcessar("Expedidor", PermissaoUtil.Modulo.USUARIOS));

        assertTrue(PermissaoUtil.podeAcessar("Auxiliar Logistico", PermissaoUtil.Modulo.CLIENTES));
        assertTrue(PermissaoUtil.podeAcessar("Expedidor", PermissaoUtil.Modulo.EXPEDICAO));
    }

    @Test
    public void perfilDesconhecidoVeApenasDashboard() {
        assertTrue(PermissaoUtil.podeAcessar("Visitante", PermissaoUtil.Modulo.DASHBOARD));
        assertFalse(PermissaoUtil.podeAcessar("Visitante", PermissaoUtil.Modulo.USUARIOS));
        assertTrue(PermissaoUtil.podeAcessar(null, PermissaoUtil.Modulo.DASHBOARD));
        assertFalse(PermissaoUtil.podeAcessar(null, PermissaoUtil.Modulo.ROTAS));
    }

    @Test
    public void normalizaMaiusculasMinusculasEEspacos() {
        assertTrue(PermissaoUtil.podeAcessar("  gErEnTe  ", PermissaoUtil.Modulo.USUARIOS));
        assertTrue(PermissaoUtil.podeAcessar("MOTORISTA", PermissaoUtil.Modulo.ROTAS));
    }

    @Test
    public void moduloNuloNuncaEhPermitido() {
        assertFalse(PermissaoUtil.podeAcessar("Gerente", null));
        assertFalse(PermissaoUtil.podeAcessar(null, null));
    }

    @Test
    public void podeGerenciarUsuariosSegueRegraDoModulo() {
        assertTrue(PermissaoUtil.podeGerenciarUsuarios("Gerente"));
        assertFalse(PermissaoUtil.podeGerenciarUsuarios("Motorista"));
        assertFalse(PermissaoUtil.podeGerenciarUsuarios("Expedidor"));
    }
}
