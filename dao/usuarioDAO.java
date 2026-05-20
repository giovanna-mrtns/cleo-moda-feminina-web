package dao;

import model.Usuario;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Autentica o usuário pelo login e senha
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT u.*, p.nome AS nome_perfil " +
                     "FROM usuario u " +
                     "LEFT JOIN perfil p ON u.id_perfil = p.id_perfil " +
                     "WHERE u.login = ? AND u.senha = ? AND u.ativo = 's'";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Lista todos os usuários com o nome do perfil
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, p.nome AS nome_perfil " +
                     "FROM usuario u " +
                     "LEFT JOIN perfil p ON u.id_perfil = p.id_perfil";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Cadastra um novo usuário
    public void cadastrar(Usuario u) {
        String sql = "INSERT INTO usuario (nome, login, senha, ativo, id_perfil) VALUES (?, ?, ?, 's', ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getLogin());
            stmt.setString(3, u.getSenha());
            stmt.setInt(4, u.getIdPerfil());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Altera os dados de um usuário existente
    public void alterar(Usuario u) {
        String sql = "UPDATE usuario SET nome = ?, login = ?, senha = ?, id_perfil = ? WHERE id_usuario = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getLogin());
            stmt.setString(3, u.getSenha());
            stmt.setInt(4, u.getIdPerfil());
            stmt.setInt(5, u.getId());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Desativa o usuário (não exclui do banco)
    public void desativar(int id) {
        String sql = "UPDATE usuario SET ativo = 'n' WHERE id_usuario = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Atualiza a data/hora do último acesso
    public void atualizarUltimoAcesso(int id) {
        String sql = "UPDATE usuario SET ultimo_acesso = NOW() WHERE id_usuario = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Altera a senha após confirmar a senha antiga
    public boolean alterarSenha(int id, String senhaAntiga, String senhaNova) {
        String sql = "UPDATE usuario SET senha = ? WHERE id_usuario = ? AND senha = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, senhaNova);
            stmt.setInt(2, id);
            stmt.setString(3, senhaAntiga);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Converte uma linha do ResultSet em objeto Usuario
    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNome(rs.getString("nome"));
        u.setLogin(rs.getString("login"));
        u.setAtivo(rs.getString("ativo"));
        u.setIdPerfil(rs.getInt("id_perfil"));
        u.setNomePerfil(rs.getString("nome_perfil"));
        Timestamp ts = rs.getTimestamp("ultimo_acesso");
        if (ts != null) u.setUltimoAcesso(ts.toLocalDateTime());
        return u;
    }
}
