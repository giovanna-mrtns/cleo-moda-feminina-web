package controller;

import java.util.List;
import model.Produto;
import model.VariacaoProduto;
import service.ProdutoService;

public class ProdutoController {

    private ProdutoService service = new ProdutoService();

    public void cadastrar(String nome, String descricao, double preco, String categoria,
                           String imagemUrl, List<VariacaoProduto> variacoes) {
        service.cadastrar(nome, descricao, preco, categoria, imagemUrl, variacoes);
    }

    public void alterar(int id, String nome, String descricao, double preco, String categoria, String imagemUrl) {
        service.alterar(id, nome, descricao, preco, categoria, imagemUrl);
    }

    public void desativar(int id) {
        service.desativar(id);
    }

    public List<Produto> listar() {
        return service.listarTodos();
    }

    public Produto buscarPorId(int id) {
        return service.buscarPorId(id);
    }

    public void adicionarVariacao(int idProduto, String tamanho, String cor, int estoque) {
        service.adicionarVariacao(idProduto, tamanho, cor, estoque);
    }

    public void alterarVariacao(int id, String tamanho, String cor, int estoque) {
        service.alterarVariacao(id, tamanho, cor, estoque);
    }

    public void removerVariacao(int id) {
        service.removerVariacao(id);
    }

    public List<VariacaoProduto> listarVariacoes(int idProduto) {
        return service.listarVariacoes(idProduto);
    }
}