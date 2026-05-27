package dao;

import model.Modulo;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuloDAO {

    // Cadastra um novo módulo
    public void cadastrar(Modulo m) {
        String sql = "INSERT INTO modulo (nome, descricao, ativo) VALUES (?, ?, 's')";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getDescricao());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Altera nome e descrição de um módulo existente
    public void alterar(Modulo m) {
        String sql = "UPDATE modulo SET nome = ?, descricao = ? WHERE id_modulo = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getDescricao());
            stmt.setInt(3, m.getId());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Desativa o módulo (não exclui do banco)
    public void desativar(int id) {
        String sql = "UPDATE modulo SET ativo = 'n' WHERE id_modulo = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Lista todos os módulos
    public List<Modulo> listarTodos() {
        List<Modulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM modulo";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Modulo m = new Modulo();
                m.setId(rs.getInt("id_modulo"));
                m.setNome(rs.getString("nome"));
                m.setDescricao(rs.getString("descricao"));
                m.setAtivo(rs.getString("ativo"));
                lista.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Busca um módulo pelo ID
    public Modulo buscarPorId(int id) {
        String sql = "SELECT * FROM modulo WHERE id_modulo = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Modulo m = new Modulo();
                    m.setId(rs.getInt("id_modulo"));
                    m.setNome(rs.getString("nome"));
                    m.setDescricao(rs.getString("descricao"));
                    m.setAtivo(rs.getString("ativo"));
                    return m;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
