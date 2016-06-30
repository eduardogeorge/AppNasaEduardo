package model;

/**
 * Created by George on 29/06/2016.
 */
public class Usuario {
    private String nome_usuario;
    private String email_usuario;
    private String id_usuario;
    private String senha_usuario;
    private String senha_antiga_usuario;
    private String senha_nova_usuario;

    public String getNome_usuario() {
        return nome_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public void setSenha_usuario(String senha_usuario) {
        this.senha_usuario = senha_usuario;
    }

    public void setSenha_antiga_usuario(String senha_antiga_usuario) {
        this.senha_antiga_usuario = senha_antiga_usuario;
    }

    public void setSenha_nova_usuario(String senha_nova_usuario) {
        this.senha_nova_usuario = senha_nova_usuario;
    }
}
