package service;

import dao.ProdutoDAO;
import model.Produto;
import java.util.List;

public class ProdutoService {

    private ProdutoDAO dao = new ProdutoDAO();

    public List<Produto> listarTodos() {
        return dao.listarTodos();
    }

    public Produto buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public void cadastrar(String nome, String descricao, double preco,
                          int estoque, String categoria, String imagemUrl) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        Produto p = new Produto();
        p.setNome(nome.trim());
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setEstoque(estoque);
        p.setCategoria(categoria);
        p.setImagemUrl(imagemUrl);
        dao.cadastrar(p);
    }

    public void alterar(int id, String nome, String descricao, double preco,
                        int estoque, String categoria, String imagemUrl) {
        Produto p = new Produto();
        p.setId(id);
        p.setNome(nome);
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setEstoque(estoque);
        p.setCategoria(categoria);
        p.setImagemUrl(imagemUrl);
        dao.alterar(p);
    }

    public void desativar(int id) {
        dao.desativar(id);
    }
}
