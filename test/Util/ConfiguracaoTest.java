package Util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import org.junit.Test;

public class ConfiguracaoTest {

    private static final Function<String, String> SEM_SISTEMA = chave -> null;

    @Test
    public void propriedadeDeSistemaTemPrecedenciaMaxima() {
        Properties arquivo = new Properties();
        arquivo.setProperty("db.usuario", "do-arquivo");
        Map<String, String> ambiente = new HashMap<>();
        ambiente.put("SGE_DB_USUARIO", "do-ambiente");

        String valor = Configuracao.resolver(
                "db.usuario", "padrao", arquivo, ambiente,
                chave -> "do-sistema");

        assertEquals("do-sistema", valor);
    }

    @Test
    public void variavelDeAmbienteVenceArquivoEPadrao() {
        Properties arquivo = new Properties();
        arquivo.setProperty("db.usuario", "do-arquivo");
        Map<String, String> ambiente = new HashMap<>();
        ambiente.put("SGE_DB_USUARIO", "do-ambiente");

        assertEquals("do-ambiente",
                Configuracao.resolver("db.usuario", "padrao", arquivo, ambiente, SEM_SISTEMA));
    }

    @Test
    public void arquivoVenceValorPadrao() {
        Properties arquivo = new Properties();
        arquivo.setProperty("db.usuario", "do-arquivo");

        assertEquals("do-arquivo",
                Configuracao.resolver("db.usuario", "padrao", arquivo, new HashMap<>(), SEM_SISTEMA));
    }

    @Test
    public void usaPadraoQuandoNadaEstaDefinido() {
        assertEquals("padrao",
                Configuracao.resolver("db.usuario", "padrao", new Properties(), new HashMap<>(), SEM_SISTEMA));
    }

    @Test
    public void ignoraValoresEmBranco() {
        Properties arquivo = new Properties();
        arquivo.setProperty("db.usuario", "   ");
        Map<String, String> ambiente = new HashMap<>();
        ambiente.put("SGE_DB_USUARIO", "  ");

        assertEquals("padrao",
                Configuracao.resolver("db.usuario", "padrao", arquivo, ambiente, chave -> "  "));
    }

    @Test
    public void removeEspacosDoValor() {
        assertEquals("valor",
                Configuracao.resolver("x", "padrao", new Properties(), new HashMap<>(),
                        chave -> "  valor  "));
    }

    @Test
    public void mapeiaChaveParaVariavelDeAmbiente() {
        assertEquals("SGE_DB_USUARIO", Configuracao.paraVariavelAmbiente("db.usuario"));
        assertEquals("SGE_DB_URL", Configuracao.paraVariavelAmbiente("db.url"));
        assertEquals("SGE_SENHA_APP", Configuracao.paraVariavelAmbiente("senha-app"));
    }

    @Test
    public void carregaArquivoDePropriedades() throws IOException {
        File arquivo = File.createTempFile("sge-config", ".properties");
        arquivo.deleteOnExit();

        Properties esperadas = new Properties();
        esperadas.setProperty("db.url", "jdbc:h2:mem:teste");
        esperadas.setProperty("db.usuario", "sa");
        try (FileOutputStream saida = new FileOutputStream(arquivo)) {
            esperadas.store(saida, "teste");
        }

        Properties carregadas = Configuracao.carregar(arquivo);

        assertEquals("jdbc:h2:mem:teste", carregadas.getProperty("db.url"));
        assertEquals("sa", carregadas.getProperty("db.usuario"));
    }

    @Test
    public void arquivoInexistenteRetornaPropriedadesVazias() {
        Properties carregadas = Configuracao.carregar(new File("/caminho/que/nao/existe.properties"));
        assertTrue(carregadas.isEmpty());
    }

    @Test
    public void getLePropriedadeDeSistema() {
        String chave = "sge.teste.configuracao";
        System.setProperty(chave, "valor-de-teste");
        try {
            assertEquals("valor-de-teste", Configuracao.get(chave, "padrao"));
        } finally {
            System.clearProperty(chave);
        }
    }
}
