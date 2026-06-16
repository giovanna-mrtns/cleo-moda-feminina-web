package model;

import java.time.LocalDateTime;

public class Usuario {

    @Expose private int id;
    @Expose private String nome;
    @Expose private String login;

    private String senha; // NÃO expor a senha no JSON!

    @Expose private LocalDateTime ultimoAcesso;
    @Expose private String ativo;    // "s" ou "n"
    @Expose private int idPerfil;    // FK para a tabela perfil
    @Expose private String nomePerfil; // para exibir na tela (preenchido via JOIN)

    public Usuario() {}

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }

    public String getAtivo() { return ativo; }
    public void setAtivo(String ativo) { this.ativo = ativo; }

    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public String getNomePerfil() { return nomePerfil; }
    public void setNomePerfil(String nomePerfil) { this.nomePerfil = nomePerfil; }
}
