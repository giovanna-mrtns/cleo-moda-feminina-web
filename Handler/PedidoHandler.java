package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.PedidoController;
import model.ItemPedido;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PedidoHandler implements HttpHandler {

    private PedidoController controller = new PedidoController();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().add("Content-Type", "application/json");

        if (ex.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            ex.sendResponseHeaders(204, -1); return;
        }

        String method = ex.getRequestMethod();
        String path   = ex.getRequestURI().getPath();
        // Exemplos de path:
        //   GET  /api/pedidos?idUsuario=2
        //   POST /api/pedidos
        //   PUT  /api/pedidos/5

        try {
            if (method.equals("GET")) {
                // Lê o parâmetro ?idUsuario=X da query string
                String query = ex.getRequestURI().getQuery(); // "idUsuario=2"
                if (query == null || !query.startsWith("idUsuario=")) {
                    byte[] msg = "{\"erro\":\"Informe o parâmetro idUsuario.\"}".getBytes(StandardCharsets.UTF_8);
                    ex.sendResponseHeaders(400, msg.length);
                    ex.getResponseBody().write(msg);
                    return;
                }
                int idUsuario = Integer.parseInt(query.replace("idUsuario=", ""));
                responder(ex, 200, gson.toJson(controller.listarPorUsuario(idUsuario)));

            } else if (method.equals("POST")) {
                // Corpo esperado: { "idUsuario": 2, "itens": [{ "idProduto": 1, "quantidade": 2, "precoUnitario": 49.90 }] }
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();

                int idUsuario = obj.get("idUsuario").getAsInt();
                JsonArray itensJson = obj.getAsJsonArray("itens");

                List<ItemPedido> itens = new ArrayList<>();
                for (JsonElement el : itensJson) {
                    JsonObject i = el.getAsJsonObject();
                    ItemPedido item = new ItemPedido();
                    item.setIdProduto(i.get("idProduto").getAsInt());
                    item.setQuantidade(i.get("quantidade").getAsInt());
                    item.setPrecoUnitario(i.get("precoUnitario").getAsDouble());
                    itens.add(item);
                }

                int idGerado = controller.criar(idUsuario, itens);
                responder(ex, 201, "{\"id\":" + idGerado + "}");

            } else if (method.equals("PUT")) {
                // PUT /api/pedidos/5  com corpo: { "status": "pago" }
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                int id         = Integer.parseInt(path.replace("/api/pedidos/", ""));
                controller.atualizarStatus(id, obj.get("status").getAsString());
                ex.sendResponseHeaders(200, -1);
            }

        } catch (IllegalArgumentException e) {
            byte[] msg = ("{\"erro\":\"" + e.getMessage() + "\"}").getBytes(StandardCharsets.UTF_8);
            ex.sendResponseHeaders(400, msg.length);
            ex.getResponseBody().write(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ex.sendResponseHeaders(500, -1);
        }

        ex.getResponseBody().close();
    }

    private void responder(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, bytes.length);
        ex.getResponseBody().write(bytes);
    }
}
