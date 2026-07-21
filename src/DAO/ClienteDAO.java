/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Cliente;
import Util.FabricaConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO {

    /**
     * Insere um novo cliente diretamente no banco de dados MySQL
     */
    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome_empresa, cnpj, telefone, email) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNomeEmpresa());
            stmt.setString(2, cliente.getCnpj());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            
            stmt.execute();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o cliente no banco de dados!", e);
        }
    }

    /**
     * Busca todos os clientes salvos no MySQL
     */
    public ArrayList<Cliente> listar() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNomeEmpresa(rs.getString("nome_empresa"));
                cliente.setCnpj(rs.getString("cnpj"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os clientes do banco de dados!", e);
        }
        
        return clientes;
    }
    
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nome_empresa = ?, cnpj = ?, telefone = ?, email = ? WHERE id = ?";
        
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNomeEmpresa());
            stmt.setString(2, cliente.getCnpj());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o cliente no banco de dados!", e);
        }
    }
    
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        Cliente cliente = null;
        
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNomeEmpresa(rs.getString("nome_empresa"));
                    cliente.setCnpj(rs.getString("cnpj"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEmail(rs.getString("email"));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID!", e);
        }
        
        return cliente;
    }
    
    public void excluir(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o cliente do banco de dados! "
                    + "Verifique se o cliente possui registros ou pedidos vinculados.", e);
        }
    }
}

