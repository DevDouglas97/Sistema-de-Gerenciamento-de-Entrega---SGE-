/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaConexao {
    
    // Configurações do seu MySQL Local
    private static final String URL = "jdbc:mysql://localhost:3306/sge?useTimezone=true&serverTimezone=UTC";
    private static final String USUARIO = "root"; // Usuário padrão do MySQL
    private static final String SENHA = "Oricode1997";       // Altere se o seu banco tiver senha (ex: XAMPP costuma ser vazio)

    /**
     * Abre uma conexão ativa com o banco de dados MySQL
     */
    public static Connection getConexao() {
        try {
            // Carrega o driver de comunicação do MySQL na memória
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do MySQL não encontrado! Adicione o JAR nas Bibliotecas.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco de dados! Verifique se o MySQL está ligado.", e);
        }
    }
}

