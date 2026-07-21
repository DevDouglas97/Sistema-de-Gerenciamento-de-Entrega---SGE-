/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Expedicao;
import Model.Produtos;
import Util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ExpedicaoDAO {

    /**
     * Salva o cabeçalho da expedição e seus itens usando transação manual.
     */
    public void salvar(Expedicao expedicao) {
        String sqlExpedicao = "INSERT INTO expedicao (cliente_id, motorista_id, status) VALUES "
                + "((SELECT id FROM clientes WHERE nome_empresa = ? LIMIT 1), "
                + "(SELECT id FROM usuarios WHERE nome = ? LIMIT 1), ?)";
        
        String sqlItem = "INSERT INTO itens_expedicao (expedicao_id, produto_id, quantidade) VALUES "
                + "(?, (SELECT id FROM produtos WHERE nome_produto = ? LIMIT 1), ?)";

        Connection conn = null;
        try {
            conn = FabricaConexao.getConexao();
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement stmtExp = conn.prepareStatement(sqlExpedicao, Statement.RETURN_GENERATED_KEYS)) {
                stmtExp.setString(1, expedicao.getCliente());
                stmtExp.setString(2, expedicao.getMotorista());
                stmtExp.setString(3, expedicao.getStatus());
                stmtExp.executeUpdate();

                int expedicaoId = 0;
                try (ResultSet rsKeys = stmtExp.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        expedicaoId = rsKeys.getInt(1);
                    }
                }

                if (expedicaoId > 0) {
                    try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
                        for (Produtos p : expedicao.getItensPedido()) {
                            stmtItem.setInt(1, expedicaoId);
                            stmtItem.setString(2, p.getNomeProduto());
                            stmtItem.setInt(3, p.getQuantidade());
                            stmtItem.addBatch();
                        }
                        stmtItem.executeBatch();
                    }
                }
            }

            conn.commit(); // Efetiva no banco
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erro ao salvar a expedição no banco de dados!", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Atualiza o cabeçalho da expedição e refaz a lista de itens vinculados.
     */
    public void atualizar(Expedicao expedicao) {
        String sqlUpdateExp = "UPDATE expedicao SET "
                + "cliente_id = (SELECT id FROM clientes WHERE nome_empresa = ? LIMIT 1), "
                + "motorista_id = (SELECT id FROM usuarios WHERE nome = ? LIMIT 1), "
                + "status = ? WHERE id = ?";

        String sqlDeleteItens = "DELETE FROM itens_expedicao WHERE expedicao_id = ?";
        
        String sqlInsertItem = "INSERT INTO itens_expedicao (expedicao_id, produto_id, quantidade) VALUES "
                + "(?, (SELECT id FROM produtos WHERE nome_produto = ? LIMIT 1), ?)";

        Connection conn = null;
        try {
            conn = FabricaConexao.getConexao();
            conn.setAutoCommit(false); // Transação para garantir integridade

            // 1. Atualiza o cabeçalho
            try (PreparedStatement stmtExp = conn.prepareStatement(sqlUpdateExp)) {
                stmtExp.setString(1, expedicao.getCliente());
                stmtExp.setString(2, expedicao.getMotorista());
                stmtExp.setString(3, expedicao.getStatus());
                stmtExp.setInt(4, expedicao.getId());
                stmtExp.executeUpdate();
            }

            // 2. Apaga os itens antigos associados a essa expedição
            try (PreparedStatement stmtDel = conn.prepareStatement(sqlDeleteItens)) {
                stmtDel.setInt(1, expedicao.getId());
                stmtDel.executeUpdate();
            }

            // 3. Insere a nova lista atualizada de itens
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlInsertItem)) {
                for (Produtos p : expedicao.getItensPedido()) {
                    stmtItem.setInt(1, expedicao.getId());
                    stmtItem.setString(2, p.getNomeProduto());
                    stmtItem.setInt(3, p.getQuantidade());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erro ao atualizar a expedição ID: " + expedicao.getId(), e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Busca todas as expedições e reconstrói as suas listas de produtos.
     */
    public ArrayList<Expedicao> listar() {
        ArrayList<Expedicao> listaExpedicoes = new ArrayList<>();

        String sqlExpedicao = "SELECT e.id, c.nome_empresa, u.nome AS nome_motorista, e.status FROM expedicao e "
                + "LEFT JOIN clientes c ON e.cliente_id = c.id "
                + "LEFT JOIN usuarios u ON e.motorista_id = u.id "
                + "ORDER BY e.id DESC";

        String sqlItens = "SELECT ie.quantidade, p.nome_produto, p.unidade FROM itens_expedicao ie "
                + "INNER JOIN produtos p ON ie.produto_id = p.id WHERE ie.expedicao_id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmtExp = conn.prepareStatement(sqlExpedicao);
             ResultSet rsExp = stmtExp.executeQuery()) {

            while (rsExp.next()) {
                Expedicao exp = new Expedicao();
                exp.setId(rsExp.getInt("id"));
                exp.setCliente(rsExp.getString("nome_empresa"));
                exp.setMotorista(rsExp.getString("nome_motorista"));
                exp.setStatus(rsExp.getString("status"));

                try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                    stmtItens.setInt(1, exp.getId());
                    try (ResultSet rsItens = stmtItens.executeQuery()) {
                        while (rsItens.next()) {
                            Produtos prod = new Produtos();
                            prod.setQuantidade(rsItens.getInt("quantidade"));
                            prod.setNomeProduto(rsItens.getString("nome_produto"));
                            prod.setUnidade(rsItens.getString("unidade"));
                            exp.adicionarItem(prod);
                        }
                    }
                }
                listaExpedicoes.add(exp);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar as expedições!", e);
        }

        return listaExpedicoes;
    }

    /**
     * Busca uma expedição específica pelo ID.
     */
    public Expedicao buscarPorId(int id) {
        String sqlExpedicao = "SELECT e.id, c.nome_empresa, u.nome AS nome_motorista, e.status FROM expedicao e "
                + "LEFT JOIN clientes c ON e.cliente_id = c.id "
                + "LEFT JOIN usuarios u ON e.motorista_id = u.id "
                + "WHERE e.id = ?";

        String sqlItens = "SELECT ie.quantidade, p.nome_produto, p.unidade FROM itens_expedicao ie "
                + "INNER JOIN produtos p ON ie.produto_id = p.id WHERE ie.expedicao_id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmtExp = conn.prepareStatement(sqlExpedicao)) {

            stmtExp.setInt(1, id);
            try (ResultSet rsExp = stmtExp.executeQuery()) {
                if (rsExp.next()) {
                    Expedicao exp = new Expedicao();
                    exp.setId(rsExp.getInt("id"));
                    exp.setCliente(rsExp.getString("nome_empresa"));
                    exp.setMotorista(rsExp.getString("nome_motorista"));
                    exp.setStatus(rsExp.getString("status"));

                    try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                        stmtItens.setInt(1, exp.getId());
                        try (ResultSet rsItens = stmtItens.executeQuery()) {
                            while (rsItens.next()) {
                                Produtos prod = new Produtos();
                                prod.setQuantidade(rsItens.getInt("quantidade"));
                                prod.setNomeProduto(rsItens.getString("nome_produto"));
                                prod.setUnidade(rsItens.getString("unidade"));
                                exp.adicionarItem(prod);
                            }
                        }
                    }
                    return exp;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar a expedição pelo ID: " + id, e);
        }

        return null;
    }

    /**
     * Exclui a expedição e remove automaticamente os itens vinculados.
     */
    public void excluir(int id) {
        String sqlDeleteItens = "DELETE FROM itens_expedicao WHERE expedicao_id = ?";
        String sqlDeleteExpedicao = "DELETE FROM expedicao WHERE id = ?";

        Connection conn = null;
        try {
            conn = FabricaConexao.getConexao();
            conn.setAutoCommit(false);

            // 1. Remove os itens filhos
            try (PreparedStatement stmtItens = conn.prepareStatement(sqlDeleteItens)) {
                stmtItens.setInt(1, id);
                stmtItens.executeUpdate();
            }

            // 2. Remove o registro pai
            try (PreparedStatement stmtExp = conn.prepareStatement(sqlDeleteExpedicao)) {
                stmtExp.setInt(1, id);
                stmtExp.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erro ao excluir a expedição ID: " + id, e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}