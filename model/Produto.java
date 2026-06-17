package model;

import java.util.List;

public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private String categoria;
    private String imagemUrl;
    private String ativo;          // "s" ou "n"
    private List<VariacaoProduto> variacoes; // preenchido via JOIN/segunda consulta na Service

    public Produto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public String getAtivo() { return ativo; }
    public void setAtivo(String ativo) { this.ativo = ativo; }

    public List<VariacaoProduto> getVariacoes() { return variacoes; }
    public void setVariacoes(List<VariacaoProduto> variacoes) { this.variacoes = variacoes; }
}