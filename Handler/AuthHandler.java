package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.UsuarioController;
import model.Usuario;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthHandler implements HttpHandler {

    private UsuarioController controller = new UsuarioController();
    private Gson gson = new Gson();

    // Mapa token → usuário (armazenado em memória enquanto o servidor estiver rodando)
    private static final Map<String, Usuario> sessoes = new ConcurrentHashMap<>();

    /** Valida um token e retorna o usuário — usado por outros Handlers se necessário */
    public static Usuario validarToken(String token) {
        if (token == null || token.isBlank()) return null;
        return sessoes.get(token);
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ex.getResponseHeaders().add("Content-Type", "application/json");

        if (ex.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            ex.sendResponseHeaders(204, -1); return;
        }

        String method = ex.getRequestMethod();
        String path   = ex.getRequestURI().getPath();

        try {
            // POST /api/auth/login → autentica e retorna token
            if (method.equals("POST") && path.endsWith("/login")) {
                String body    = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();

                Usuario usuario = controller.login(
                    obj.get("login").getAsString(),
                    obj.get("senha").getAsString()
                );

                if (usuario == null) {
                    byte[] msg = "{\"erro\":\"Login ou senha inválidos.\"}".getBytes(StandardCharsets.UTF_8);
                    ex.sendResponseHeaders(401, msg.length);
                    ex.getResponseBody().write(msg);
                    return;
                }

                String token = UUID.randomUUID().toString();
                sessoes.put(token, usuario);

                // Retorna o token e os dados básicos do usuário
                JsonObject resposta = new JsonObject();
                resposta.addProperty("token", token);
                resposta.addProperty("id", usuario.getId());
                resposta.addProperty("nome", usuario.getNome());
                resposta.addProperty("login", usuario.getLogin());
                resposta.addProperty("nomePerfil", usuario.getNomePerfil());
                responder(ex, 200, gson.toJson(resposta));

            // POST /api/auth/logout → invalida o token
            } else if (method.equals("POST") && path.endsWith("/logout")) {
                String token = extrairToken(ex);
                sessoes.remove(token);
                ex.sendResponseHeaders(200, -1);

            // GET /api/auth/me → retorna os dados do usuário logado pelo token
            } else if (method.equals("GET") && path.endsWith("/me")) {
                String token   = extrairToken(ex);
                Usuario usuario = validarToken(token);
                if (usuario == null) {
                    ex.sendResponseHeaders(401, -1); return;
                }
                responder(ex, 200, gson.toJson(usuario));

            } else {
                ex.sendResponseHeaders(404, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ex.sendResponseHeaders(500, -1);
        }

        ex.getResponseBody().close();
    }

    // Lê o token do header Authorization: Bearer <token>
    private String extrairToken(HttpExchange ex) {
        String auth = ex.getRequestHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return null;
    }

    private void responder(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, bytes.length);
        ex.getResponseBody().write(bytes);
    }
}
