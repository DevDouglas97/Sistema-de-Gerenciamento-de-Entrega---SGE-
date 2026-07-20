/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Rotas {

    private int id;
    private String pedidoDisplay; // Guarda a identificação do pedido (ex: "#1")
    private String cliente;
    private String motorista;
    private String enderecoEntrega;
    private String status = "Pendente"; // Automaticamente criado como Pendente

    // Construtor Vazio
    public Rotas() {
    }

    // Construtor Completo
    public Rotas(int id, String pedidoDisplay, String cliente, String motorista, String enderecoEntrega, String status) {
        this.id = id;
        this.pedidoDisplay = pedidoDisplay;
        this.cliente = cliente;
        this.motorista = motorista;
        this.enderecoEntrega = enderecoEntrega;
        this.status = status;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPedidoDisplay() {
        return pedidoDisplay;
    }

    public void setPedidoDisplay(String pedidoDisplay) {
        this.pedidoDisplay = pedidoDisplay;
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

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

