/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Produtos {

    private int id;
    private String cliente; 
    private String nomeProduto;
    private int quantidade; 
    private String unidade; 
    private String categoria; 

    // Construtor Vazio
    public Produtos() {
    }

    // Construtor Completo
    public Produtos(int id, String cliente, String nomeProduto, int quantidade, String unidade, String categoria) {
        this.id = id;
        this.cliente = cliente;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.categoria = categoria;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int cantidad) {
        this.quantidade = cantidad;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
