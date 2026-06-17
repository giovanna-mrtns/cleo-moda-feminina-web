package dao;

import static util.Conexao.getConexao;
import model.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    // Lista os endereços de um usuário, com o principal aparecendo primeiro
    public List<Endereco> listarPorUsuario(int idUsuario) {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM endereco WHERE id_usuario = ? ORDER BY principal DESC, id DESC";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Insere um novo endereço e retorna o ID gerado
    public int cadastrar(Endereco e) {
        String sql = "INSERT INTO endereco " +
                     "(id_usuario, apelido, nome_destinatario, rua, numero, bairro, cidade, estado, cep, telefone, principal) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(stmt, e);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return -1;
    }

    // Atualiza um endereço existente (garante que pertence ao usuário informado)
    public void alterar(Endereco e) {
        String sql = "UPDATE endereco SET apelido = ?, nome_destinatario = ?, rua = ?, numero = ?, " +
                     "bairro = ?, cidade = ?, estado = ?, cep = ?, telefone = ?, principal = ? " +
                     "WHERE id = ? AND id_usuario = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getApelido());
            stmt.setString(2, e.getNomeDestinatario());
            stmt.setString(3, e.getRua());
            stmt.setString(4, e.getNumero());
            stmt.setString(5, e.getBairro());
            stmt.setString(6, e.getCidade());
            stmt.setString(7, e.getEstado());
            stmt.setString(8, e.getCep());
            stmt.setString(9, e.getTelefone());
            stmt.setBoolean(10, e.isPrincipal());
            stmt.setInt(11, e.getId());
            stmt.setInt(12, e.getIdUsuario());
            stmt.executeUpdate();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // Exclui o endereço; se era o principal, promove outro automaticamente
    public void excluir(int id) {
        try (Connection conn = getConexao()) {
            int idUsuario = -1;
            try (PreparedStatement stmt = conn.prepareStatement("SELECT id_usuario FROM endereco WHERE id = ?")) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) idUsuario = rs.getInt("id_usuario");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM endereco WHERE id = ?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            if (idUsuario == -1) return;

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM endereco WHERE id_usuario = ? AND principal = TRUE")) {
                stmt.setInt(1, idUsuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) return; // ainda existe um principal, não faz nada
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id FROM endereco WHERE id_usuario = ? ORDER BY id LIMIT 1")) {
                stmt.setInt(1, idUsuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement marca = conn.prepareStatement(
                                "UPDATE endereco SET principal = TRUE WHERE id = ?")) {
                            marca.setInt(1, rs.getInt("id"));
                            marca.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // Garante que só um endereço do usuário fique marcado como principal
    public void definirPrincipal(int idUsuario, int idEndereco) {
        try (Connection conn = getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE endereco SET principal = FALSE WHERE id_usuario = ?")) {
                stmt.setInt(1, idUsuario);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE endereco SET principal = TRUE WHERE id = ? AND id_usuario = ?")) {
                stmt.setInt(1, idEndereco);
                stmt.setInt(2, idUsuario);
                stmt.executeUpdate();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void preencherParametros(PreparedStatement stmt, Endereco e) throws SQLException {
        stmt.setInt(1, e.getIdUsuario());
        stmt.setString(2, e.getApelido());
        stmt.setString(3, e.getNomeDestinatario());
        stmt.setString(4, e.getRua());
        stmt.setString(5, e.getNumero());
        stmt.setString(6, e.getBairro());
        stmt.setString(7, e.getCidade());
        stmt.setString(8, e.getEstado());
        stmt.setString(9, e.getCep());
        stmt.setString(10, e.getTelefone());
        stmt.setBoolean(11, e.isPrincipal());
    }

    private Endereco mapear(ResultSet rs) throws SQLException {
        Endereco e = new Endereco();
        e.setId(rs.getInt("id"));
        e.setIdUsuario(rs.getInt("id_usuario"));
        e.setApelido(rs.getString("apelido"));
        e.setNomeDestinatario(rs.getString("nome_destinatario"));
        e.setRua(rs.getString("rua"));
        e.setNumero(rs.getString("numero"));
        e.setBairro(rs.getString("bairro"));
        e.setCidade(rs.getString("cidade"));
        e.setEstado(rs.getString("estado"));
        e.setCep(rs.getString("cep"));
        e.setTelefone(rs.getString("telefone"));
        e.setPrincipal(rs.getBoolean("principal"));
        return e;
    }
}