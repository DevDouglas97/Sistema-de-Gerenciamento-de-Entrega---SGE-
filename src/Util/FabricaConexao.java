/*
 * Fábrica de conexões JDBC.
 *
 * As credenciais não ficam mais no código. Elas são lidas de Configuracao,
 * que aceita variáveis de ambiente, config.properties ou -Dpropriedade.
 */
package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FabricaConexao {

    private static final Logger LOGGER = Logger.getLogger(FabricaConexao.class.getName());

    private FabricaConexao() {
    }

    /**
     * Abre uma conexão ativa com o banco de dados configurado.
     */
    public static Connection getConexao() {
        String driver = Configuracao.getDriver();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Driver JDBC não encontrado: " + driver
                    + ". Verifique a pasta lib/ e o arquivo config.properties.", e);
        }

        try {
            return DriverManager.getConnection(
                    Configuracao.getUrl(),
                    Configuracao.getUsuario(),
                    Configuracao.getSenha());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao conectar no banco de dados", e);
            throw new IllegalStateException(
                    "Erro ao conectar no banco de dados! Verifique a configuração e se o MySQL está ligado.", e);
        }
    }
}
