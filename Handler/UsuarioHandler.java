package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.UsuarioController;
import model.Usuario;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class UsuarioHandler implements HttpHandler {
    private UsuarioController controller = new UsuarioController();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        // CORS — permite o frontend acessar
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().add("Content-Type", "application/json");

        if (ex.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            ex.sendResponseHeaders(204, -1); return;
        }

        String method = ex.getRequestMethod();
        String path   = ex.getRequestURI().getPath(); // ex: /api/usuarios/2

        if (method.equals("GET")) {
            String json = gson.toJson(controller.listar());
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            ex.sendResponseHeaders(200, bytes.length);
            ex.getResponseBody().write(bytes);

        } else if (method.equals("POST")) {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
            controller.cadastrar(
                obj.get("nome").getAsString(),
                obj.get("login").getAsString(),
                obj.get("senha").getAsString(),
                obj.get("idPerfil").getAsInt()
            );
            ex.sendResponseHeaders(201, -1);

        } else if (method.equals("PUT")) {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
            controller.alterar(
                obj.get("id").getAsInt(),
                obj.get("nome").getAsString(),
                obj.get("login").getAsString(),
                obj.get("senha").getAsString(),
                obj.get("idPerfil").getAsInt()
            );
            ex.sendResponseHeaders(200, -1);

        } else if (method.equals("DELETE")) {
            int id = Integer.parseInt(path.replace("/api/usuarios/", ""));
            controller.desativar(id);
            ex.sendResponseHeaders(200, -1);
        }

        ex.getResponseBody().close();
    }
}