/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;

public class Expedicao {

    private int id;
    private String cliente;
    private String motorista;
    private String status = "Aguardando";
    private ArrayList<Produtos> itensPedido;

    // Construtor Vazio
    public Expedicao() {
        this.itensPedido = new ArrayList<>();
    }

    // Construtor Completo
    public Expedicao(int id, String cliente, String motorista, String status, ArrayList<Produtos> itensPedido) {
        this.id = id;
        this.cliente = cliente;
        this.motorista = motorista;
        this.status = status;
        this.itensPedido = itensPedido;
    }

    // Getters e Setters garantidos para a compilação
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

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Produtos> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(ArrayList<Produtos> itensPedido) {
        this.itensPedido = itensPedido;
    }
    
    public void adicionarItem(Produtos produto) {
        this.itensPedido.add(produto);
    }
}
