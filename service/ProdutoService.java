package service;

import dao.ProdutoDAO;
import dao.VariacaoProdutoDAO;
import model.Produto;
import model.VariacaoProduto;
import java.util.List;

public class ProdutoService {

    private ProdutoDAO dao = new ProdutoDAO();
    private VariacaoProdutoDAO variacaoDao = new VariacaoProdutoDAO();

    // Cadastra um produto novo junto com suas variações iniciais (tamanho/cor)
    public void cadastrar(String nome, String descricao, double preco, String categoria,
                           String imagemUrl, List<VariacaoProduto> variacoes) {
        if (nome == null || nome.isBlank()) {
            System.out.println("Erro: o nome do produto é obrigatório.");
            return;
        }
        if (variacoes == null || variacoes.isEmpty()) {
            System.out.println("Erro: o produto precisa de ao menos uma variação (tamanho/cor).");
            return;
        }

        Produto p = new Produto();
        p.setNome(nome.trim());
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setCategoria(categoria);
        p.setImagemUrl(imagemUrl);

        int idGerado = dao.cadastrar(p);
        if (idGerado == -1) return;

        for (VariacaoProduto v : variacoes) {
            v.setIdProduto(idGerado);
            variacaoDao.cadastrar(v);
        }
    }

    // Altera os dados gerais do produto (as variações são geridas separadamente)
    public void alterar(int id, String nome, String descricao, double preco, String categoria, String imagemUrl) {
        Produto p = new Produto();
        p.setId(id);
        p.setNome(nome.trim());
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setCategoria(categoria);
        p.setImagemUrl(imagemUrl);
        dao.alterar(p);
    }

    // Desativa um produto
    public void desativar(int id) {
        dao.desativar(id);
    }

    // Lista todos os produtos, cada um já com sua lista de variações anexada
    public List<Produto> listarTodos() {
        List<Produto> produtos = dao.listarTodos();
        for (Produto p : produtos) {
            p.setVariacoes(variacaoDao.listarPorProduto(p.getId()));
        }
        return produtos;
    }

    // Busca um produto pelo ID, já com suas variações
    public Produto buscarPorId(int id) {
        Produto p = dao.buscarPorId(id);
        if (p != null) {
            p.setVariacoes(variacaoDao.listarPorProduto(id));
        }
        return p;
    }

    // ---- Gestão de variações (tamanho/cor) de um produto já existente ----

    public void adicionarVariacao(int idProduto, String tamanho, String cor, int estoque) {
        VariacaoProduto v = new VariacaoProduto(tamanho, cor, estoque);
        v.setIdProduto(idProduto);
        variacaoDao.cadastrar(v);
    }

    public void alterarVariacao(int id, String tamanho, String cor, int estoque) {
        VariacaoProduto v = new VariacaoProduto(tamanho, cor, estoque);
        v.setId(id);
        variacaoDao.alterar(v);
    }

    public void removerVariacao(int id) {
        variacaoDao.remover(id);
    }

    public List<VariacaoProduto> listarVariacoes(int idProduto) {
        return variacaoDao.listarPorProduto(idProduto);
    }
}