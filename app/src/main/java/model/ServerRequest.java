package model;

/**
 * Created by George on 29/06/2016.
 */
public class ServerRequest {
    private String operacao;
    private Usuario usuario;

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
