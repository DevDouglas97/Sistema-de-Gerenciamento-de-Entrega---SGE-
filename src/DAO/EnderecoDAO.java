/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Endereco;
import Util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EnderecoDAO {

    /**
     * Insere um novo endereço.
     * Usa clienteId quando disponível; caso contrário, mantém o fallback por nome
     * para não quebrar telas antigas.
     */
    public void salvar(Endereco endereco) {
        String sql = "INSERT INTO enderecos (cliente_id, descricao, endereco, bairro, cidade_uf) "
                   + "VALUES (COALESCE(NULLIF(?, 0), "
                   + "(SELECT id FROM clientes WHERE nome_empresa = ? LIMIT 1)), ?, ?, ?, ?)";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, endereco.getClienteId());
            stmt.setString(2, endereco.getCliente());
            stmt.setString(3, endereco.getDescricao());
            stmt.setString(4, endereco.getEndereco());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidadeUf());

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o endereço no banco de dados!", e);
        }
    }

    /**
     * Atualiza os dados de um endereço existente no banco.
     */
    public void atualizar(Endereco endereco) {
        String sql = "UPDATE enderecos SET "
                   + "cliente_id = COALESCE(NULLIF(?, 0), "
                   + "(SELECT id FROM clientes WHERE nome_empresa = ? LIMIT 1)), "
                   + "descricao = ?, endereco = ?, bairro = ?, cidade_uf = ? "
                   + "WHERE id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, endereco.getClienteId());
            stmt.setString(2, endereco.getCliente());
            stmt.setString(3, endereco.getDescricao());
            stmt.setString(4, endereco.getEndereco());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidadeUf());
            stmt.setInt(7, endereco.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o endereço no banco de dados!", e);
        }
    }

    /**
     * Busca todos os endereços trazendo o nome do cliente associado via JOIN.
     */
    public ArrayList<Endereco> listar() {
        ArrayList<Endereco> enderecos = new ArrayList<>();

        String sql = "SELECT e.*, c.nome_empresa FROM enderecos e "
                   + "INNER JOIN clientes c ON e.cliente_id = c.id";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                enderecos.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os endereços do banco de dados!", e);
        }

        return enderecos;
    }

    /**
     * Busca um endereço específico através do seu ID.
     */
    public Endereco buscarPorId(int id) {
        String sql = "SELECT e.*, c.nome_empresa FROM enderecos e "
                   + "INNER JOIN clientes c ON e.cliente_id = c.id "
                   + "WHERE e.id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o endereço por ID!", e);
        }

        return null;
    }

    /**
     * Remove um endereço do banco de dados pelo seu ID.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM enderecos WHERE id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o endereço no banco de dados!", e);
        }
    }

    private Endereco mapear(ResultSet rs) throws SQLException {
        Endereco enderecoObj = new Endereco();
        enderecoObj.setId(rs.getInt("id"));
        enderecoObj.setClienteId(rs.getInt("cliente_id"));
        enderecoObj.setCliente(rs.getString("nome_empresa"));
        enderecoObj.setDescricao(rs.getString("descricao"));
        enderecoObj.setEndereco(rs.getString("endereco"));
        enderecoObj.setBairro(rs.getString("bairro"));
        enderecoObj.setCidadeUf(rs.getString("cidade_uf"));
        return enderecoObj;
    }
}
