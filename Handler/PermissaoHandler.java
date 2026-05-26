package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.PermissaoController;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PermissaoHandler implements HttpHandler {
    private PermissaoController controller = new PermissaoController();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().add("Content-Type", "application/json");

        if (ex.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            ex.sendResponseHeaders(204, -1); return;
        }

        String method = ex.getRequestMethod();
        String query  = ex.getRequestURI().getQuery(); // ex: idPerfil=2

        try {
            if (method.equals("GET")) {
                // Retorna permissões de um perfil — GET /api/permissoes?idPerfil=2
                int idPerfil = Integer.parseInt(query.replace("idPerfil=", ""));
                String json  = gson.toJson(controller.listarPorPerfil(idPerfil));
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(200, bytes.length);
                ex.getResponseBody().write(bytes);

            } else if (method.equals("POST")) {
                // Salva permissão — body: { "idPerfil":1, "idModulo":2,
                //                           "visualizar":true, "editar":false, "excluir":false }
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                controller.salvar(
                    obj.get("idPerfil").getAsInt(),
                    obj.get("idModulo").getAsInt(),
                    obj.get("visualizar").getAsBoolean(),
                    obj.get("editar").getAsBoolean(),
                    obj.get("excluir").getAsBoolean()
                );
                ex.sendResponseHeaders(200, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ex.sendResponseHeaders(500, -1);
        }
        ex.getResponseBody().close();
    }
}