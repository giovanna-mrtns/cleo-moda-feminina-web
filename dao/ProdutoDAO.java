package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Produto;
import static util.Conexao.getConexao;

public class ProdutoDAO {

    // Cadastra um novo produto (as variações são tratadas separadamente).
    // Retorna o ID gerado, ou -1 se algo falhar.
    public int cadastrar(Produto p) {
        String sql = "INSERT INTO produto (nome, descricao, preco, categoria, imagem_url, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, 's')";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setDouble(3, p.getPreco());
            stmt.setString(4, p.getCategoria());
            stmt.setString(5, p.getImagemUrl());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // Altera os dados gerais de um produto existente
    public void alterar(Produto p) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, categoria = ?, imagem_url = ? WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setDouble(3, p.getPreco());
            stmt.setString(4, p.getCategoria());
            stmt.setString(5, p.getImagemUrl());
            stmt.setInt(6, p.getId());
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

    // Lista todos os produtos ativos (sem variações — a Service cuida de juntar)
    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE ativo = 's'";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
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

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setPreco(rs.getDouble("preco"));
        p.setCategoria(rs.getString("categoria"));
        p.setImagemUrl(rs.getString("imagem_url"));
        p.setAtivo(rs.getString("ativo"));
        return p;
    }
}