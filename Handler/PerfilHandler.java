package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.PerfilController;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PerfilHandler implements HttpHandler {
    private PerfilController controller = new PerfilController();
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
        String path   = ex.getRequestURI().getPath(); // ex: /api/perfis/3

        try {
            if (method.equals("GET")) {
                // Lista todos os perfis
                String json  = gson.toJson(controller.listar());
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(200, bytes.length);
                ex.getResponseBody().write(bytes);

            } else if (method.equals("POST")) {
                // Cadastra novo perfil — body: { "nome": "..." }
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                controller.cadastrar(obj.get("nome").getAsString());
                ex.sendResponseHeaders(201, -1);

            } else if (method.equals("PUT")) {
                // Altera perfil — body: { "id": 1, "nome": "..." }
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                controller.alterar(
                    obj.get("id").getAsInt(),
                    obj.get("nome").getAsString()
                );
                ex.sendResponseHeaders(200, -1);

            } else if (method.equals("DELETE")) {
                // Desativa perfil — URL: /api/perfis/3
                int id = Integer.parseInt(path.replace("/api/perfis/", ""));
                controller.desativar(id);
                ex.sendResponseHeaders(200, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ex.sendResponseHeaders(500, -1);
        }
        ex.getResponseBody().close();
    }
}