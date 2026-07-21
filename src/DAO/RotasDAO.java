/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Rotas;
import Util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RotasDAO {

    /**
     * Insere uma nova rota vinculando ao ID do pedido e ID do endereço no MySQL
     */
    public void salvar(Rotas rota) {
        // Extrai apenas o número do texto (Ex: "#1" vira "1") para usar como ID da expedição
        String idPedidoTexto = rota.getPedidoDisplay().replace("#", "").trim();
        int expedicaoId = Integer.parseInt(idPedidoTexto);

        // A subquery descobre o endereco_id com base no texto da rua vindo da tela
        String sql = "INSERT INTO rotas (expedicao_id, endereco_id, status) "
                   + "VALUES (?, (SELECT id FROM enderecos WHERE endereco = ? LIMIT 1), ?)";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expedicaoId);
            stmt.setString(2, rota.getEnderecoEntrega());
            stmt.setString(3, rota.getStatus());

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a rota no banco de dados!", e);
        }
    }

    /**
     * Atualiza os dados de uma rota existente pelo seu ID.
     */
    public void atualizar(Rotas rota) {
        String sql = "UPDATE rotas SET "
                   + "endereco_id = (SELECT id FROM enderecos WHERE endereco = ? LIMIT 1), "
                   + "status = ? "
                   + "WHERE id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rota.getEnderecoEntrega());
            stmt.setString(2, rota.getStatus());
            stmt.setInt(3, rota.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a rota no banco de dados!", e);
        }
    }

    /**
     * Busca todas as rotas trazendo os textos correspondentes via JOINs.
     * Usa LEFT JOIN para motorista para não ocultar registros pendentes de atribuição.
     */
    public ArrayList<Rotas> listar() {
        ArrayList<Rotas> listaRotas = new ArrayList<>();

        String sql = "SELECT r.id, r.expedicao_id, c.nome_empresa, e.endereco, "
                   + "COALESCE(u.nome, 'Não Atribuído') AS nome_motorista, r.status "
                   + "FROM rotas r "
                   + "INNER JOIN expedicao ex ON r.expedicao_id = ex.id "
                   + "INNER JOIN clientes c ON ex.cliente_id = c.id "
                   + "INNER JOIN enderecos e ON r.endereco_id = e.id "
                   + "LEFT JOIN usuarios u ON ex.motorista_id = u.id";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rotas rotaObj = new Rotas();
                rotaObj.setId(rs.getInt("id"));
                rotaObj.setPedidoDisplay("#" + rs.getInt("expedicao_id"));
                rotaObj.setCliente(rs.getString("nome_empresa"));
                rotaObj.setEnderecoEntrega(rs.getString("endereco"));
                rotaObj.setMotorista(rs.getString("nome_motorista"));
                rotaObj.setStatus(rs.getString("status"));

                listaRotas.add(rotaObj);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar as rotas do banco de dados!", e);
        }

        return listaRotas;
    }

    /**
     * Busca uma única rota pelo ID.
     */
    public Rotas buscarPorId(int id) {
        String sql = "SELECT r.id, r.expedicao_id, c.nome_empresa, e.endereco, "
                   + "COALESCE(u.nome, 'Não Atribuído') AS nome_motorista, r.status "
                   + "FROM rotas r "
                   + "INNER JOIN expedicao ex ON r.expedicao_id = ex.id "
                   + "INNER JOIN clientes c ON ex.cliente_id = c.id "
                   + "INNER JOIN enderecos e ON r.endereco_id = e.id "
                   + "LEFT JOIN usuarios u ON ex.motorista_id = u.id "
                   + "WHERE r.id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rotas rotaObj = new Rotas();
                    rotaObj.setId(rs.getInt("id"));
                    rotaObj.setPedidoDisplay("#" + rs.getInt("expedicao_id"));
                    rotaObj.setCliente(rs.getString("nome_empresa"));
                    rotaObj.setEnderecoEntrega(rs.getString("endereco"));
                    rotaObj.setMotorista(rs.getString("nome_motorista"));
                    rotaObj.setStatus(rs.getString("status"));
                    return rotaObj;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar a rota por ID!", e);
        }

        return null;
    }

    /**
     * Remove uma rota do banco de dados pelo seu ID.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM rotas WHERE id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir a rota!", e);
        }
    }
}