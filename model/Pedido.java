package model;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private int id;
    private int idUsuario;
    private String nomeUsuario;       // preenchido via JOIN
    private LocalDateTime dataPedido;
    private String status;            // "pendente" | "pago" | "enviado" | "cancelado"
    private double total;
    private List<ItemPedido> itens;   // preenchido pela DAO

    public Pedido() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}
