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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RotasDAO {

    /**
     * Insere uma nova rota usando os IDs de expedição/endereço quando disponíveis.
     * O fallback textual mantém compatibilidade com chamadas antigas.
     */
    public void salvar(Rotas rota) {
        int expedicaoId = resolverExpedicaoId(rota);

        String sql = "INSERT INTO rotas (expedicao_id, endereco_id, status) VALUES "
                   + "(?, COALESCE(NULLIF(?, 0), "
                   + "(SELECT id FROM enderecos WHERE endereco = ? LIMIT 1)), ?)";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expedicaoId);
            stmt.setInt(2, rota.getEnderecoId());
            stmt.setString(3, rota.getEnderecoEntrega());
            stmt.setString(4, rota.getStatus());

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
                   + "endereco_id = COALESCE(NULLIF(?, 0), "
                   + "(SELECT id FROM enderecos WHERE endereco = ? LIMIT 1)), "
                   + "status = ? "
                   + "WHERE id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rota.getEnderecoId());
            stmt.setString(2, rota.getEnderecoEntrega());
            stmt.setString(3, rota.getStatus());
            stmt.setInt(4, rota.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a rota no banco de dados!", e);
        }
    }

    /**
     * Busca todas as rotas trazendo os textos correspondentes via JOINs.
     */
    public ArrayList<Rotas> listar() {
        ArrayList<Rotas> listaRotas = new ArrayList<>();

        String sql = "SELECT r.id, r.expedicao_id, r.endereco_id, c.nome_empresa, e.endereco, "
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
                listaRotas.add(mapear(rs));
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
        String sql = "SELECT r.id, r.expedicao_id, r.endereco_id, c.nome_empresa, e.endereco, "
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
                    return mapear(rs);
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

    /**
     * Define o ID da expedição. Se não vier preenchido, tenta extrair o primeiro
     * número do texto de exibição ("Pedido #12" -> 12).
     */
    int resolverExpedicaoId(Rotas rota) {
        if (rota.getExpedicaoId() > 0) {
            return rota.getExpedicaoId();
        }

        String display = rota.getPedidoDisplay();
        if (display != null) {
            Matcher matcher = Pattern.compile("\\d+").matcher(display);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
        }

        throw new IllegalArgumentException(
                "Não foi possível identificar o pedido da rota. Informe o expedicaoId.");
    }

    private Rotas mapear(ResultSet rs) throws SQLException {
        Rotas rotaObj = new Rotas();
        rotaObj.setId(rs.getInt("id"));
        rotaObj.setExpedicaoId(rs.getInt("expedicao_id"));
        rotaObj.setEnderecoId(rs.getInt("endereco_id"));
        rotaObj.setPedidoDisplay("#" + rotaObj.getExpedicaoId());
        rotaObj.setCliente(rs.getString("nome_empresa"));
        rotaObj.setEnderecoEntrega(rs.getString("endereco"));
        rotaObj.setMotorista(rs.getString("nome_motorista"));
        rotaObj.setStatus(rs.getString("status"));
        return rotaObj;
    }
}
