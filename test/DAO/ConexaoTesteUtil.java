package DAO;

import Util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Infraestrutura compartilhada dos testes de DAO.
 * Usa H2 em memória no modo de compatibilidade MySQL.
 */
final class ConexaoTesteUtil {

    static final String URL =
            "jdbc:h2:mem:sge;MODE=MySQL;DB_CLOSE_DELAY=-1";

    private ConexaoTesteUtil() {
    }

    static void configurar() {
        System.setProperty("db.driver", "org.h2.Driver");
        System.setProperty("db.url", URL);
        System.setProperty("db.usuario", "sa");
        System.setProperty("db.senha", "");
    }

    static void recriarSchema() {
        configurar();

        try (Connection conn = FabricaConexao.getConexao();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP ALL OBJECTS");

            stmt.execute("CREATE TABLE clientes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nome_empresa VARCHAR(150),"
                    + "cnpj VARCHAR(20),"
                    + "telefone VARCHAR(30),"
                    + "email VARCHAR(120))");

            stmt.execute("CREATE TABLE usuarios ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nome VARCHAR(120),"
                    + "cpf VARCHAR(20),"
                    + "login VARCHAR(60),"
                    + "senha VARCHAR(255),"
                    + "perfil VARCHAR(40),"
                    + "ativo BOOLEAN)");

            stmt.execute("CREATE TABLE enderecos ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "cliente_id INT,"
                    + "descricao VARCHAR(80),"
                    + "endereco VARCHAR(200),"
                    + "bairro VARCHAR(100),"
                    + "cidade_uf VARCHAR(100))");

            stmt.execute("CREATE TABLE produtos ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "cliente_id INT,"
                    + "nome_produto VARCHAR(120),"
                    + "quantidade INT,"
                    + "unidade VARCHAR(10),"
                    + "categoria VARCHAR(60))");

            stmt.execute("CREATE TABLE expedicao ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "cliente_id INT,"
                    + "motorista_id INT,"
                    + "status VARCHAR(40))");

            stmt.execute("CREATE TABLE itens_expedicao ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "expedicao_id INT,"
                    + "produto_id INT,"
                    + "quantidade INT)");

            stmt.execute("CREATE TABLE rotas ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "expedicao_id INT,"
                    + "endereco_id INT,"
                    + "status VARCHAR(20))");

        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao preparar o banco de teste", e);
        }
    }

    static int inserir(String sql, Object... parametros) {
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            definir(stmt, parametros);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao inserir no banco de teste: " + sql, e);
        }
    }

    static void executar(String sql, Object... parametros) {
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            definir(stmt, parametros);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao executar no banco de teste: " + sql, e);
        }
    }

    static int contar(String tabela) {
        return inteiro("SELECT COUNT(*) FROM " + tabela);
    }

    static int inteiro(String sql, Object... parametros) {
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            definir(stmt, parametros);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha na consulta de teste: " + sql, e);
        }
    }

    static String texto(String sql, Object... parametros) {
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            definir(stmt, parametros);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha na consulta de teste: " + sql, e);
        }
    }

    private static void definir(PreparedStatement stmt, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            Object valor = parametros[i];
            if (valor == null) {
                stmt.setObject(i + 1, null);
            } else if (valor instanceof Integer) {
                stmt.setInt(i + 1, (Integer) valor);
            } else if (valor instanceof Boolean) {
                stmt.setBoolean(i + 1, (Boolean) valor);
            } else {
                stmt.setString(i + 1, valor.toString());
            }
        }
    }
}
