package dao;

import static util.Conexao.getConexao;
import model.Perfil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerfilDAO {

    // Cadastra um novo perfil
    public void cadastrar(Perfil p) {
        String sql = "INSERT INTO perfil (nome, ativo) VALUES (?, 's')";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Altera o nome de um perfil existente
    public void alterar(Perfil p) {
        String sql = "UPDATE perfil SET nome = ? WHERE id = ?";  // CORRIGIDO: era id_perfil
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setInt(2, p.getId());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Desativa o perfil (não exclui do banco)
    public void desativar(int id) {
        String sql = "UPDATE perfil SET ativo = 'n' WHERE id = ?";  // CORRIGIDO: era id_perfil
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Lista todos os perfis ativos
    public List<Perfil> listarTodos() {
        List<Perfil> lista = new ArrayList<>();
        String sql = "SELECT * FROM perfil WHERE ativo = 's'";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Perfil p = new Perfil();
                p.setId(rs.getInt("id"));           // CORRIGIDO: era id_perfil
                p.setNome(rs.getString("nome"));
                p.setAtivo(rs.getString("ativo"));
                lista.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Busca um perfil pelo ID
    public Perfil buscarPorId(int id) {
        String sql = "SELECT * FROM perfil WHERE id = ?";  // CORRIGIDO: era id_perfil
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Perfil p = new Perfil();
                    p.setId(rs.getInt("id"));       // CORRIGIDO: era id_perfil
                    p.setNome(rs.getString("nome"));
                    p.setAtivo(rs.getString("ativo"));
                    return p;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
