/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Usuario;
import Util.FabricaConexao;
import Util.SenhaUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO {

    /**
     * Insere um novo usuário com a senha já protegida por hash.
     */
    public void salvar(Usuario usuario) {
        String senhaPreparada = prepararSenha(usuario.getSenha());
        if (senhaPreparada == null) {
            throw new IllegalArgumentException("A senha é obrigatória para um novo usuário.");
        }

        String sql = "INSERT INTO usuarios (nome, cpf, login, senha, perfil, ativo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, senhaPreparada);
            stmt.setString(5, usuario.getPerfil());
            stmt.setBoolean(6, usuario.isAtivo());

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o usuário no banco de dados!", e);
        }
    }

    /**
     * Retorna todos os usuários. A senha não é exposta para a camada de visão.
     */
    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapear(rs, false));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os usuários do banco de dados!", e);
        }

        return usuarios;
    }

    /**
     * Autentica o usuário comparando a senha informada com o hash armazenado.
     * Senhas legadas em texto puro são migradas automaticamente no login.
     */
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND ativo = TRUE";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            Usuario usuario = null;
            String hashArmazenado = null;

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hashArmazenado = rs.getString("senha");
                    if (!SenhaUtil.verificar(senha, hashArmazenado)) {
                        return null;
                    }
                    usuario = mapear(rs, false);
                }
            }

            if (usuario != null && SenhaUtil.precisaMigrar(hashArmazenado)) {
                atualizarHash(conn, usuario.getId(), senha);
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário no banco de dados!", e);
        }
    }

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

    /**
     * Atualiza o usuário. Se a senha vier vazia, mantém a atual (útil na edição).
     * Se vier em texto puro, gera um novo hash.
     */
    public void atualizar(Usuario usuario) {
        String senhaPreparada = prepararSenha(usuario.getSenha());

        String sql;
        if (senhaPreparada == null) {
            sql = "UPDATE usuarios SET nome = ?, cpf = ?, login = ?, perfil = ?, ativo = ? WHERE id = ?";
        } else {
            sql = "UPDATE usuarios SET nome = ?, cpf = ?, login = ?, senha = ?, perfil = ?, ativo = ? WHERE id = ?";
        }

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int indice = 1;
            stmt.setString(indice++, usuario.getNome());
            stmt.setString(indice++, usuario.getCpf());
            stmt.setString(indice++, usuario.getLogin());

            if (senhaPreparada != null) {
                stmt.setString(indice++, senhaPreparada);
            }

            stmt.setString(indice++, usuario.getPerfil());
            stmt.setBoolean(indice++, usuario.isAtivo());
            stmt.setInt(indice, usuario.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o usuário no banco de dados!", e);
        }
    }

    private void atualizarHash(Connection conn, int id, String senhaPura) throws SQLException {
        String sql = "UPDATE usuarios SET senha = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, SenhaUtil.gerarHash(senhaPura));
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Converte para hash quando necessário. Retorna null para "não alterar".
     */
    private String prepararSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            return null;
        }
        if (SenhaUtil.estaHasheada(senha)) {
            return senha;
        }
        return SenhaUtil.gerarHash(senha);
    }

    private Usuario mapear(ResultSet rs, boolean incluirSenha) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setLogin(rs.getString("login"));
        usuario.setPerfil(rs.getString("perfil"));
        usuario.setAtivo(rs.getBoolean("ativo"));
        if (incluirSenha) {
            usuario.setSenha(rs.getString("senha"));
        }
        return usuario;
    }
}
