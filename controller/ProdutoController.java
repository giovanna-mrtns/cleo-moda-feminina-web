package controller;

import model.Produto;
import service.ProdutoService;
import java.util.List;

public class ProdutoController {

    private ProdutoService service = new ProdutoService();

    public List<Produto> listar() {
        return service.listarTodos();
    }

    public Produto buscarPorId(int id) {
        return service.buscarPorId(id);
    }

    public void cadastrar(String nome, String descricao, double preco,
                          int estoque, String categoria, String imagemUrl) {
        service.cadastrar(nome, descricao, preco, estoque, categoria, imagemUrl);
    }

    public void alterar(int id, String nome, String descricao, double preco,
                        int estoque, String categoria, String imagemUrl) {
        service.alterar(id, nome, descricao, preco, estoque, categoria, imagemUrl);
    }

    public void desativar(int id) {
        service.desativar(id);
    }
}
