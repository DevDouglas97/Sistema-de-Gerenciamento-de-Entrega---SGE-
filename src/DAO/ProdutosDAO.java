/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Produtos;
import Util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutosDAO {

    /**
     * Insere um novo produto. Usa clienteId quando disponível e mantém o
     * fallback por nome para compatibilidade.
     */
    public void salvar(Produtos produto) {
        String sql = "INSERT INTO produtos (cliente_id, nome_produto, quantidade, unidade, categoria) "
                   + "VALUES (COALESCE(NULLIF(?, 0), "
                   + "(SELECT id FROM clientes WHERE nome_empresa = ? LIMIT 1)), ?, ?, ?, ?)";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getClienteId());
            stmt.setString(2, produto.getCliente());
            stmt.setString(3, produto.getNomeProduto());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setString(5, produto.getUnidade());
            stmt.setString(6, produto.getCategoria());

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o produto no banco de dados!", e);
        }
    }

    /**
     * Atualiza um produto existente.
     */
    public void atualizar(Produtos produto) {
        String sql = "UPDATE produtos SET "
                   + "cliente_id = COALESCE(NULLIF(?, 0), "
                   + "(SELECT id FROM clientes WHERE nome_empresa = ? LIMIT 1)), "
                   + "nome_produto = ?, quantidade = ?, unidade = ?, categoria = ? "
                   + "WHERE id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getClienteId());
            stmt.setString(2, produto.getCliente());
            stmt.setString(3, produto.getNomeProduto());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setString(5, produto.getUnidade());
            stmt.setString(6, produto.getCategoria());
            stmt.setInt(7, produto.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o produto no banco de dados!", e);
        }
    }

    /**
     * Busca todos os produtos trazendo o nome da empresa dona via INNER JOIN.
     */
    public ArrayList<Produtos> listar() {
        ArrayList<Produtos> produtos = new ArrayList<>();

        String sql = "SELECT p.*, c.nome_empresa FROM produtos p "
                   + "INNER JOIN clientes c ON p.cliente_id = c.id";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os produtos do banco de dados!", e);
        }

        return produtos;
    }

    /**
     * Busca um único produto pelo ID para preencher os campos na tela de edição.
     */
    public Produtos buscarPorId(int id) {
        String sql = "SELECT p.*, c.nome_empresa FROM produtos p "
                   + "INNER JOIN clientes c ON p.cliente_id = c.id "
                   + "WHERE p.id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o produto por ID!", e);
        }

        return null;
    }

    /**
     * Remove um produto do banco de dados a partir do seu ID.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o produto no banco de dados!", e);
        }
    }

    private Produtos mapear(ResultSet rs) throws SQLException {
        Produtos produtoObj = new Produtos();
        produtoObj.setId(rs.getInt("id"));
        produtoObj.setClienteId(rs.getInt("cliente_id"));
        produtoObj.setCliente(rs.getString("nome_empresa"));
        produtoObj.setNomeProduto(rs.getString("nome_produto"));
        produtoObj.setQuantidade(rs.getInt("quantidade"));
        produtoObj.setUnidade(rs.getString("unidade"));
        produtoObj.setCategoria(rs.getString("categoria"));
        return produtoObj;
    }
}
