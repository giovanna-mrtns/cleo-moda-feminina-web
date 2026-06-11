package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.ProdutoController;
import model.Produto;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ProdutoHandler implements HttpHandler {

    private ProdutoController controller = new ProdutoController();
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
        String path   = ex.getRequestURI().getPath(); // ex: /api/produtos ou /api/produtos/3

        try {
            if (method.equals("GET")) {
                // GET /api/produtos/3  → busca por ID
                // GET /api/produtos    → lista todos
                if (path.matches("/api/produtos/\\d+")) {
                    int id = Integer.parseInt(path.replace("/api/produtos/", ""));
                    Produto produto = controller.buscarPorId(id);
                    if (produto == null) {
                        ex.sendResponseHeaders(404, -1); return;
                    }
                    responder(ex, 200, gson.toJson(produto));
                } else {
                    responder(ex, 200, gson.toJson(controller.listar()));
                }

            } else if (method.equals("POST")) {
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                controller.cadastrar(
                    obj.get("nome").getAsString(),
                    obj.has("descricao") ? obj.get("descricao").getAsString() : "",
                    obj.get("preco").getAsDouble(),
                    obj.has("estoque") ? obj.get("estoque").getAsInt() : 0,
                    obj.has("categoria") ? obj.get("categoria").getAsString() : "",
                    obj.has("imagemUrl") ? obj.get("imagemUrl").getAsString() : ""
                );
                ex.sendResponseHeaders(201, -1);

            } else if (method.equals("PUT")) {
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                controller.alterar(
                    obj.get("id").getAsInt(),
                    obj.get("nome").getAsString(),
                    obj.has("descricao") ? obj.get("descricao").getAsString() : "",
                    obj.get("preco").getAsDouble(),
                    obj.has("estoque") ? obj.get("estoque").getAsInt() : 0,
                    obj.has("categoria") ? obj.get("categoria").getAsString() : "",
                    obj.has("imagemUrl") ? obj.get("imagemUrl").getAsString() : ""
                );
                ex.sendResponseHeaders(200, -1);

            } else if (method.equals("DELETE")) {
                int id = Integer.parseInt(path.replace("/api/produtos/", ""));
                controller.desativar(id);
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
