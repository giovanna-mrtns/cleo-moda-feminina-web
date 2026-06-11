package service;

import dao.PedidoDAO;
import model.Pedido;
import model.ItemPedido;
import java.util.List;

public class PedidoService {

    private PedidoDAO dao = new PedidoDAO();

    private static final List<String> STATUS_VALIDOS =
        List.of("pendente", "pago", "enviado", "cancelado");

    public List<Pedido> listarPorUsuario(int idUsuario) {
        return dao.listarPorUsuario(idUsuario);
    }

    public int criar(int idUsuario, List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve ter pelo menos um item.");
        }
        double total = itens.stream()
            .mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade())
            .sum();

        Pedido pedido = new Pedido();
        pedido.setIdUsuario(idUsuario);
        pedido.setTotal(total);
        pedido.setItens(itens);
        return dao.criar(pedido);
    }

    public void atualizarStatus(int id, String status) {
        if (!STATUS_VALIDOS.contains(status)) {
            throw new IllegalArgumentException(
                "Status inválido. Use: pendente, pago, enviado ou cancelado."
            );
        }
        dao.atualizarStatus(id, status);
    }
}
