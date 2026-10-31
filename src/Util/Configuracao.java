/*
 * Configuração central do sistema.
 *
 * Precedência (da maior para a menor):
 *   1. Propriedade de sistema Java (-Ddb.url=...)
 *   2. Variável de ambiente (SGE_DB_URL, SGE_DB_USUARIO, SGE_DB_SENHA, SGE_DB_DRIVER)
 *   3. Arquivo config.properties (na raiz do projeto ou no classpath)
 *   4. Valor padrão
 *
 * Nenhuma credencial real deve ficar versionada no código-fonte.
 */
package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public final class Configuracao {

    public static final String CHAVE_URL = "db.url";
    public static final String CHAVE_USUARIO = "db.usuario";
    public static final String CHAVE_SENHA = "db.senha";
    public static final String CHAVE_DRIVER = "db.driver";

    public static final String ARQUIVO_PADRAO = "config.properties";

    public static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/sge?useTimezone=true&serverTimezone=UTC";
    public static final String USUARIO_PADRAO = "root";
    public static final String SENHA_PADRAO = "";
    public static final String DRIVER_PADRAO = "com.mysql.cj.jdbc.Driver";

    private static final Properties PROPRIEDADES = carregarArquivoPadrao();

    private Configuracao() {
    }

    public static String get(String chave, String padrao) {
        return resolver(chave, padrao, PROPRIEDADES, System.getenv(), System::getProperty);
    }

    public static String getDriver() {
        return get(CHAVE_DRIVER, DRIVER_PADRAO);
    }

    public static String getUrl() {
        return get(CHAVE_URL, URL_PADRAO);
    }

    public static String getUsuario() {
        return get(CHAVE_USUARIO, USUARIO_PADRAO);
    }

    public static String getSenha() {
        return get(CHAVE_SENHA, SENHA_PADRAO);
    }

    /**
     * Resolve um valor respeitando a ordem de precedência. Método exposto para testes.
     */
    static String resolver(String chave, String padrao, Properties props,
            Map<String, String> ambiente, Function<String, String> propriedadesSistema) {
        String valor = propriedadesSistema == null ? null : propriedadesSistema.apply(chave);
        if (preenchido(valor)) {
            return valor.trim();
        }

        if (ambiente != null) {
            valor = ambiente.get(paraVariavelAmbiente(chave));
            if (preenchido(valor)) {
                return valor.trim();
            }
        }

        if (props != null) {
            valor = props.getProperty(chave);
            if (preenchido(valor)) {
                return valor.trim();
            }
        }

        return padrao;
    }

    /**
     * "db.usuario" vira "SGE_DB_USUARIO".
     */
    static String paraVariavelAmbiente(String chave) {
        return "SGE_" + chave.toUpperCase(Locale.ROOT)
                .replace('.', '_')
                .replace('-', '_');
    }

    /**
     * Carrega um arquivo de propriedades. Retorna vazio quando o arquivo não existe.
     */
    static Properties carregar(File arquivo) {
        Properties propriedades = new Properties();
        if (arquivo == null || !arquivo.isFile()) {
            return propriedades;
        }
        try (InputStream entrada = new FileInputStream(arquivo)) {
            propriedades.load(entrada);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível ler o arquivo de configuração: " + arquivo.getAbsolutePath(), e);
        }
        return propriedades;
    }

    private static Properties carregarArquivoPadrao() {
        String caminho = System.getProperty("sge.config", ARQUIVO_PADRAO);
        Properties propriedades = carregar(new File(caminho));

        if (propriedades.isEmpty()) {
            try (InputStream entrada = Configuracao.class.getClassLoader()
                    .getResourceAsStream(ARQUIVO_PADRAO)) {
                if (entrada != null) {
                    propriedades.load(entrada);
                }
            } catch (IOException e) {
                throw new IllegalStateException(
                        "Não foi possível ler " + ARQUIVO_PADRAO + " do classpath.", e);
            }
        }
        return propriedades;
    }

    private static boolean preenchido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
}
