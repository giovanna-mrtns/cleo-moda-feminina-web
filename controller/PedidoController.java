package controller;

import model.Pedido;
import model.ItemPedido;
import service.PedidoService;
import java.util.List;

public class PedidoController {

    private PedidoService service = new PedidoService();

    public List<Pedido> listarPorUsuario(int idUsuario) {
        return service.listarPorUsuario(idUsuario);
    }

    public int criar(int idUsuario, List<ItemPedido> itens) {
        return service.criar(idUsuario, itens);
    }

    public void atualizarStatus(int id, String status) {
        service.atualizarStatus(id, status);
    }
}
