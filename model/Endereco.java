package model;

import com.google.gson.annotations.Expose;

public class Endereco {

    @Expose private int id;
    @Expose private int idUsuario;
    @Expose private String apelido;
    @Expose private String nomeDestinatario;
    @Expose private String rua;
    @Expose private String numero;
    @Expose private String bairro;
    @Expose private String cidade;
    @Expose private String estado;
    @Expose private String cep;
    @Expose private String telefone;
    @Expose private boolean principal;

    public Endereco() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getApelido() { return apelido; }
    public void setApelido(String apelido) { this.apelido = apelido; }

    public String getNomeDestinatario() { return nomeDestinatario; }
    public void setNomeDestinatario(String nomeDestinatario) { this.nomeDestinatario = nomeDestinatario; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }
}