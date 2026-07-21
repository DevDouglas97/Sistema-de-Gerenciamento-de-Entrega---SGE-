/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Usuario;
import Util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO {

    /**
     * Insere um novo usuário diretamente na tabela do MySQL
     */
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, cpf, login, senha, perfil, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        
        // Abre a conexão automaticamente e garante o fechamento com o try-with-resources
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Mapeia os dados do objeto nos pontos de interrogação (?) do SQL
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getPerfil());
            stmt.setBoolean(6, usuario.isAtivo());
            
            stmt.execute(); // Executa o comando no banco de dados
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o usuário no banco de dados!", e);
        }
    }

    /**
     * Retorna todos os usuários gravados no MySQL para carregar na JTable
     */
    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql =   "SELECT * FROM usuarios";
        
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // Percorre todas as linhas retornadas pelo banco de dados
            while (rs.next()) {
                Usuario usuario = new Usuario();
                
                // Pega os dados das colunas do MySQL e coloca no objeto Java
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                usuario.setAtivo(rs.getBoolean("ativo"));
                
                usuarios.add(usuario); // Adiciona o usuário na lista que vai para a tela
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os usuários do banco de dados!", e);
        }
        
        return usuarios;
    }
    
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ? AND ativo = TRUE";
        
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, login);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setPerfil(rs.getString("perfil"));
                    usuario.setAtivo(rs.getBoolean("ativo"));
                    return usuario; // Usuário encontrado e autenticado com sucesso
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário no banco de dados!", e);
        }
        return null; // Login ou senha inválidos
    }
    
    // No UsuarioDAO, adicione:
    public void excluir(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário!", e);
        }
    }
    
    public void atualizar(Usuario usuario) {
    String sql = "UPDATE usuarios SET nome = ?, cpf = ?, login = ?, senha = ?, perfil = ?, ativo = ? WHERE id = ?";
    
    try (Connection conn = FabricaConexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, usuario.getNome());
        stmt.setString(2, usuario.getCpf());
        stmt.setString(3, usuario.getLogin());
        stmt.setString(4, usuario.getSenha());
        stmt.setString(5, usuario.getPerfil());
        stmt.setBoolean(6, usuario.isAtivo());
        stmt.setInt(7, usuario.getId()); // ID no WHERE para atualizar o registro correto
        
        stmt.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o usuário no banco de dados!", e);
        }
    }
}
