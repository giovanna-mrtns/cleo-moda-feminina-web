package dao;

import static util.Conexao.getConexao;
import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // Lista todos os produtos ativos
    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE ativo = 's' ORDER BY nome";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Busca um produto pelo ID
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Cadastra um novo produto
    public void cadastrar(Produto p) {
        String sql = "INSERT INTO produto (nome, descricao, preco, estoque, categoria, imagem_url, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 's')";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setDouble(3, p.getPreco());
            stmt.setInt(4, p.getEstoque());
            stmt.setString(5, p.getCategoria());
            stmt.setString(6, p.getImagemUrl());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Altera os dados de um produto existente
    public void alterar(Produto p) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, estoque = ?, " +
                     "categoria = ?, imagem_url = ? WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setDouble(3, p.getPreco());
            stmt.setInt(4, p.getEstoque());
            stmt.setString(5, p.getCategoria());
            stmt.setString(6, p.getImagemUrl());
            stmt.setInt(7, p.getId());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Desativa o produto (não exclui do banco)
    public void desativar(int id) {
        String sql = "UPDATE produto SET ativo = 'n' WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Converte uma linha do ResultSet em objeto Produto
    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setPreco(rs.getDouble("preco"));
        p.setEstoque(rs.getInt("estoque"));
        p.setCategoria(rs.getString("categoria"));
        p.setImagemUrl(rs.getString("imagem_url"));
        p.setAtivo(rs.getString("ativo"));
        return p;
    }
}
