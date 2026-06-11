package dao;

import static util.Conexao.getConexao;
import model.Pedido;
import model.ItemPedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // Lista todos os pedidos de um usuário com os itens
    public List<Pedido> listarPorUsuario(int idUsuario) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.*, u.nome AS nome_usuario " +
                     "FROM pedido p " +
                     "LEFT JOIN usuario u ON p.id_usuario = u.id " +
                     "WHERE p.id_usuario = ? " +
                     "ORDER BY p.data_pedido DESC";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = mapear(rs);
                    pedido.setItens(listarItensDoPedido(conn, pedido.getId()));
                    lista.add(pedido);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Cria um novo pedido e retorna o ID gerado
    public int criar(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_usuario, data_pedido, status, total) " +
                     "VALUES (?, NOW(), 'pendente', ?) RETURNING id";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getIdUsuario());
            stmt.setDouble(2, pedido.getTotal());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    inserirItens(conn, idGerado, pedido.getItens());
                    return idGerado;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // Atualiza o status de um pedido (ex: "pago", "enviado", "cancelado")
    public void atualizarStatus(int id, String status) {
        String sql = "UPDATE pedido SET status = ? WHERE id = ?";
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Insere os itens de um pedido recém criado
    private void inserirItens(Connection conn, int idPedido, List<ItemPedido> itens) throws Exception {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco_unitario) " +
                     "VALUES (?, ?, ?, ?)";
        for (ItemPedido item : itens) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idPedido);
                stmt.setInt(2, item.getIdProduto());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getPrecoUnitario());
                stmt.executeUpdate();
            }
        }
    }

    // Carrega os itens de um pedido com o nome do produto via JOIN
    private List<ItemPedido> listarItensDoPedido(Connection conn, int idPedido) throws Exception {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT ip.*, pr.nome AS nome_produto " +
                     "FROM item_pedido ip " +
                     "LEFT JOIN produto pr ON ip.id_produto = pr.id " +
                     "WHERE ip.id_pedido = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setId(rs.getInt("id"));
                    item.setIdPedido(rs.getInt("id_pedido"));
                    item.setIdProduto(rs.getInt("id_produto"));
                    item.setNomeProduto(rs.getString("nome_produto"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                    itens.add(item);
                }
            }
        }
        return itens;
    }

    // Converte uma linha do ResultSet em objeto Pedido
    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setIdUsuario(rs.getInt("id_usuario"));
        p.setNomeUsuario(rs.getString("nome_usuario"));
        p.setStatus(rs.getString("status"));
        p.setTotal(rs.getDouble("total"));
        Timestamp ts = rs.getTimestamp("data_pedido");
        if (ts != null) p.setDataPedido(ts.toLocalDateTime());
        return p;
    }
}
