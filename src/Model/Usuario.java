/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Usuario {

    private int id;
    private String nome;
    private String cpf; // Adicionado para corrigir o erro de compilação
    private String login;
    private String senha;
    private String perfil;
    private boolean ativo;

    // Construtor Vazio
    public Usuario() {
    }

    // Construtor Completo Atualizado
    public Usuario(int id, String nome, String cpf, String login,
            String senha, String perfil, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf; // Inicializa o CPF
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    // Métodos Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter do CPF
    public String getCpf() {
        return cpf;
    }

    // Setter do CPF exigido pela tela CadastroUsuarios
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
