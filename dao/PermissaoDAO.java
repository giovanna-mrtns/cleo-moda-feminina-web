package dao;

import model.Permissao;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissaoDAO {

    // Salva ou atualiza uma permissão.
    // Se já existir uma linha para esse par (id_perfil, id_modulo), atualiza.
    // Se não existir, insere uma nova. Isso funciona graças ao UNIQUE KEY no SQL.
    public void salvar(Permissao p) {
    String check = "SELECT COUNT(*) FROM permissao WHERE id_perfil = ? AND id_modulo = ?";
    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(check)) {
        stmt.setInt(1, p.getIdPerfil());
        stmt.setInt(2, p.getIdModulo());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        boolean existe = rs.getInt(1) > 0;

        String sql = existe
            ? "UPDATE permissao SET pode_visualizar=?, pode_editar=?, pode_excluir=? WHERE id_perfil=? AND id_modulo=?"
            : "INSERT INTO permissao (pode_visualizar, pode_editar, pode_excluir, id_perfil, id_modulo) VALUES (?,?,?,?,?)";

        try (PreparedStatement s2 = conn.prepareStatement(sql)) {
            s2.setBoolean(1, p.isPodeVisualizar());
            s2.setBoolean(2, p.isPodeEditar());
            s2.setBoolean(3, p.isPodeExcluir());
            s2.setInt(4, p.getIdPerfil());
            s2.setInt(5, p.getIdModulo());
            s2.executeUpdate();
        }
    } catch (Exception e) { e.printStackTrace(); }
}

    // Busca todas as permissões de um perfil,
    // fazendo JOIN com a tabela modulo para trazer o nome do módulo
    public List<Permissao> listarPorPerfil(int idPerfil) {
        List<Permissao> lista = new ArrayList<>();
        String sql = "SELECT p.*, m.nome AS nome_modulo " +
                     "FROM permissao p " +
                     "JOIN modulo m ON p.id_modulo = m.id_modulo " +
                     "WHERE p.id_perfil = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPerfil);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Permissao perm = new Permissao();
                    perm.setId(rs.getInt("id_permissao"));
                    perm.setIdPerfil(rs.getInt("id_perfil"));
                    perm.setIdModulo(rs.getInt("id_modulo"));
                    perm.setPodeVisualizar(rs.getBoolean("pode_visualizar"));
                    perm.setPodeEditar(rs.getBoolean("pode_editar"));
                    perm.setPodeExcluir(rs.getBoolean("pode_excluir"));
                    perm.setNomeModulo(rs.getString("nome_modulo"));
                    lista.add(perm);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }
}
