package Util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SenhaUtilTest {

    // Iterações baixas deixam o teste rápido; o algoritmo é o mesmo.
    private static final int ITERACOES_TESTE = 1_000;

    @Test
    public void hashNaoGuardaSenhaEmTextoPuro() {
        String hash = SenhaUtil.gerarHash("minhaSenha123", ITERACOES_TESTE);

        assertNotNull(hash);
        assertNotEquals("minhaSenha123", hash);
        assertTrue(hash.startsWith("pbkdf2$sha256$"));
        assertFalse(hash.contains("minhaSenha123"));
    }

    @Test
    public void verificaSenhaCorreta() {
        String hash = SenhaUtil.gerarHash("senha-secreta", ITERACOES_TESTE);

        assertTrue(SenhaUtil.verificar("senha-secreta", hash));
    }

    @Test
    public void rejeitaSenhaIncorreta() {
        String hash = SenhaUtil.gerarHash("senha-secreta", ITERACOES_TESTE);

        assertFalse(SenhaUtil.verificar("senha-errada", hash));
        assertFalse(SenhaUtil.verificar("", hash));
        assertFalse(SenhaUtil.verificar(null, hash));
        assertFalse(SenhaUtil.verificar("senha-secreta", null));
    }

    @Test
    public void geraSaisDiferentesParaAMesmaSenha() {
        String primeiro = SenhaUtil.gerarHash("mesma-senha", ITERACOES_TESTE);
        String segundo = SenhaUtil.gerarHash("mesma-senha", ITERACOES_TESTE);

        assertNotEquals(primeiro, segundo);
        assertTrue(SenhaUtil.verificar("mesma-senha", primeiro));
        assertTrue(SenhaUtil.verificar("mesma-senha", segundo));
    }

    @Test
    public void aceitaSenhaLegadaEmTextoPuro() {
        assertTrue(SenhaUtil.verificar("123456", "123456"));
        assertFalse(SenhaUtil.verificar("1234567", "123456"));
    }

    @Test
    public void identificaNecessidadeDeMigracao() {
        assertTrue(SenhaUtil.precisaMigrar("123456"));
        assertTrue(SenhaUtil.precisaMigrar(null));
        assertFalse(SenhaUtil.precisaMigrar(
                SenhaUtil.gerarHash("x", ITERACOES_TESTE)));
    }

    @Test
    public void identificaHashValido() {
        String hash = SenhaUtil.gerarHash("x", ITERACOES_TESTE);

        assertTrue(SenhaUtil.estaHasheada(hash));
        assertFalse(SenhaUtil.estaHasheada("texto-puro"));
        assertFalse(SenhaUtil.estaHasheada(null));
    }

    @Test
    public void rejeitaHashMalformadoSemLancarExcecao() {
        assertFalse(SenhaUtil.verificar("x", "pbkdf2$sha256$abc"));
        assertFalse(SenhaUtil.verificar("x", "pbkdf2$sha256$1000$@@@$@@@"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejeitaSenhaNulaAoGerarHash() {
        SenhaUtil.gerarHash(null);
    }
}
