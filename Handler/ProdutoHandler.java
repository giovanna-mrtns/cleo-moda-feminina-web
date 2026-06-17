package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.ProdutoController;
import model.Produto;
import model.VariacaoProduto;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
            ex.sendResponseHeaders(204, -1);
            return;
        }

        String method = ex.getRequestMethod();
        String path   = ex.getRequestURI().getPath();

        // Remove o prefixo "/api/produtos" e separa o resto em partes.
        // Ex: "/api/produtos/5/variacoes/12" -> ["5", "variacoes", "12"]
        String resto = path.replaceFirst("^/api/produtos/?", "");
        String[] partes = resto.isEmpty() ? new String[0] : resto.split("/");

        try {
            if (partes.length == 0) {
                tratarRaiz(ex, method);                                   // /api/produtos

            } else if (partes.length == 1) {
                tratarProdutoEspecifico(ex, method, Integer.parseInt(partes[0])); // /api/produtos/{id}

            } else if (partes.length == 2 && partes[1].equals("variacoes")) {
                tratarVariacoes(ex, method, Integer.parseInt(partes[0]));  // /api/produtos/{id}/variacoes

            } else if (partes.length == 3 && partes[1].equals("variacoes")) {
                tratarVariacaoEspecifica(ex, method, Integer.parseInt(partes[2])); // /api/produtos/{id}/variacoes/{idVariacao}

            } else {
                ex.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ex.sendResponseHeaders(500, -1);
        }

        ex.getResponseBody().close();
    }

    // ---- /api/produtos ----
    private void tratarRaiz(HttpExchange ex, String method) throws IOException {
        if (method.equals("GET")) {
            enviar(ex, 200, gson.toJson(controller.listar()));

        } else if (method.equals("POST")) {
            JsonObject obj = lerCorpo(ex);
            controller.cadastrar(
                obj.get("nome").getAsString(),
                obj.has("descricao") ? obj.get("descricao").getAsString() : null,
                obj.get("preco").getAsDouble(),
                obj.has("categoria") ? obj.get("categoria").getAsString() : null,
                obj.has("imagemUrl") ? obj.get("imagemUrl").getAsString() : null,
                lerVariacoes(obj)
            );
            ex.sendResponseHeaders(201, -1);

        } else if (method.equals("PUT")) {
            JsonObject obj = lerCorpo(ex);
            controller.alterar(
                obj.get("id").getAsInt(),
                obj.get("nome").getAsString(),
                obj.has("descricao") ? obj.get("descricao").getAsString() : null,
                obj.get("preco").getAsDouble(),
                obj.has("categoria") ? obj.get("categoria").getAsString() : null,
                obj.has("imagemUrl") ? obj.get("imagemUrl").getAsString() : null
            );
            ex.sendResponseHeaders(200, -1);

        } else {
            ex.sendResponseHeaders(405, -1);
        }
    }

    // ---- /api/produtos/{id} ----
    private void tratarProdutoEspecifico(HttpExchange ex, String method, int id) throws IOException {
        if (method.equals("GET")) {
            Produto p = controller.buscarPorId(id);
            if (p == null) { ex.sendResponseHeaders(404, -1); return; }
            enviar(ex, 200, gson.toJson(p));

        } else if (method.equals("DELETE")) {
            controller.desativar(id);
            ex.sendResponseHeaders(200, -1);

        } else {
            ex.sendResponseHeaders(405, -1);
        }
    }

    // ---- /api/produtos/{id}/variacoes ----
    private void tratarVariacoes(HttpExchange ex, String method, int idProduto) throws IOException {
        if (method.equals("GET")) {
            enviar(ex, 200, gson.toJson(controller.listarVariacoes(idProduto)));

        } else if (method.equals("POST")) {
            JsonObject obj = lerCorpo(ex);
            controller.adicionarVariacao(
                idProduto,
                obj.get("tamanho").getAsString(),
                obj.get("cor").getAsString(),
                obj.get("estoque").getAsInt()
            );
            ex.sendResponseHeaders(201, -1);

        } else {
            ex.sendResponseHeaders(405, -1);
        }
    }

    // ---- /api/produtos/{id}/variacoes/{idVariacao} ----
    private void tratarVariacaoEspecifica(HttpExchange ex, String method, int idVariacao) throws IOException {
        if (method.equals("PUT")) {
            JsonObject obj = lerCorpo(ex);
            controller.alterarVariacao(
                idVariacao,
                obj.get("tamanho").getAsString(),
                obj.get("cor").getAsString(),
                obj.get("estoque").getAsInt()
            );
            ex.sendResponseHeaders(200, -1);

        } else if (method.equals("DELETE")) {
            controller.removerVariacao(idVariacao);
            ex.sendResponseHeaders(200, -1);

        } else {
            ex.sendResponseHeaders(405, -1);
        }
    }

    // ---- utilitários ----
    private JsonObject lerCorpo(HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return JsonParser.parseString(body).getAsJsonObject();
    }

    private List<VariacaoProduto> lerVariacoes(JsonObject obj) {
        List<VariacaoProduto> lista = new ArrayList<>();
        if (obj.has("variacoes")) {
            for (JsonElement el : obj.get("variacoes").getAsJsonArray()) {
                JsonObject v = el.getAsJsonObject();
                lista.add(new VariacaoProduto(
                    v.get("tamanho").getAsString(),
                    v.get("cor").getAsString(),
                    v.get("estoque").getAsInt()
                ));
            }
        }
        return lista;
    }

    private void enviar(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, bytes.length);
        ex.getResponseBody().write(bytes);
    }
}