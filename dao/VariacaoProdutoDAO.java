package dao;

import static util.Conexao.getConexao;
import model.VariacaoProduto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VariacaoProdutoDAO {

    // Cadastra uma nova variação (tamanho/cor) para um produto
    public void cadastrar(VariacaoProduto v) {
        String sql = "INSERT INTO variacao_produto (id_produto, tamanho, cor, estoque) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getIdProduto());
            stmt.setString(2, v.getTamanho());
            stmt.setString(3, v.getCor());
            stmt.setInt(4, v.getEstoque());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Altera tamanho, cor e estoque de uma variação existente
    public void alterar(VariacaoProduto v) {
        String sql = "UPDATE variacao_produto SET tamanho = ?, cor = ?, estoque = ? WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.getTamanho());
            stmt.setString(2, v.getCor());
            stmt.setInt(3, v.getEstoque());
            stmt.setInt(4, v.getId());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Remove uma variação definitivamente (aqui não existe "ativo", é exclusão real)
    public void remover(int id) {
        String sql = "DELETE FROM variacao_produto WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Lista todas as variações de um produto específico
    public List<VariacaoProduto> listarPorProduto(int idProduto) {
        List<VariacaoProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM variacao_produto WHERE id_produto = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Busca uma variação específica pelo ID
    public VariacaoProduto buscarPorId(int id) {
        String sql = "SELECT * FROM variacao_produto WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    private VariacaoProduto mapear(ResultSet rs) throws SQLException {
        VariacaoProduto v = new VariacaoProduto();
        v.setId(rs.getInt("id"));
        v.setIdProduto(rs.getInt("id_produto"));
        v.setTamanho(rs.getString("tamanho"));
        v.setCor(rs.getString("cor"));
        v.setEstoque(rs.getInt("estoque"));
        return v;
    }
}