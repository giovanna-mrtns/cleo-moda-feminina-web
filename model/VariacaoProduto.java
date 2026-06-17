package model;

public class VariacaoProduto {

    private int id;
    private int idProduto;   // FK para a tabela produto
    private String tamanho;
    private String cor;
    private int estoque;     // estoque desta combinação específica de tamanho/cor

    public VariacaoProduto() {}

    public VariacaoProduto(String tamanho, String cor, int estoque) {
        this.tamanho = tamanho;
        this.cor = cor;
        this.estoque = estoque;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }
}