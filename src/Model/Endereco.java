/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Endereco {

    private int id;
    private String cliente; // Nome do cliente selecionado no combobox
    private String descricao; // Ex: "Sede", "Filial", "Depósito"
    private String endereco; // Rua, número, etc.
    private String bairro;
    private String cidadeUf;

    // Construtor Vazio
    public Endereco() {
    }

    // Construtor Completo
    public Endereco(int id, String cliente, String descricao, String endereco, String bairro, String cidadeUf) {
        this.id = id;
        this.cliente = cliente;
        this.descricao = descricao;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidadeUf = cidadeUf;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidadeUf() {
        return cidadeUf;
    }

    public void setCidadeUf(String cidadeUf) {
        this.cidadeUf = cidadeUf;
    }
}
