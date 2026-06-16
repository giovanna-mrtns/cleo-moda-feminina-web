package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.UsuarioController;
import model.Usuario;
import java.io.*;
import java.nio.charset.StandardCharsets;
import util.LocalDateTimeAdapter;
import java.time.LocalDateTime;


public class UsuarioHandler implements HttpHandler {

private UsuarioController controller = new UsuarioController();
private Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().add("Content-Type", "application/json");

        if (ex.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            ex.sendResponseHeaders(204, -1);
            return;
        }

        String method = ex.getRequestMethod();
        String path   = ex.getRequestURI().getPath();

        try {
            if (method.equals("GET")) {
                String json  = gson.toJson(controller.listar());
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(200, bytes.length);
                ex.getResponseBody().write(bytes);

            } else if (method.equals("POST")) {
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();

                if (path.endsWith("/login")) {
                    // Autenticação
                    Usuario u = controller.login(
                        obj.get("login").getAsString(),
                        obj.get("senha").getAsString()
                    );
                    if (u != null) {
                        byte[] bytes = gson.toJson(u).getBytes(StandardCharsets.UTF_8);
                        ex.sendResponseHeaders(200, bytes.length);
                        ex.getResponseBody().write(bytes);
                    } else {
                        ex.sendResponseHeaders(401, -1);
                    }

                } else {
                    // Cadastro — trata e-mail duplicado com 409
                    try {
                        controller.cadastrar(
                            obj.get("nome").getAsString(),
                            obj.get("login").getAsString(),
                            obj.get("senha").getAsString(),
                            obj.get("idPerfil").getAsInt()
                        );
                        ex.sendResponseHeaders(201, -1);
                    } catch (Exception cadastroEx) {
                        byte[] msg = "{\"erro\":\"E-mail já cadastrado.\"}".getBytes(StandardCharsets.UTF_8);
                        ex.sendResponseHeaders(409, msg.length);
                        ex.getResponseBody().write(msg);
                    }
                }

            } else if (method.equals("PUT")) {
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
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

        } catch (Exception e) {
            e.printStackTrace();
            ex.sendResponseHeaders(500, -1);
        }

        ex.getResponseBody().close();
    }
}
