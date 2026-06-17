package handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.EnderecoController;
import model.Endereco;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EnderecoHandler implements HttpHandler {

    private EnderecoController controller = new EnderecoController();
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

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
        String query  = ex.getRequestURI().getQuery();

        try {
            if (method.equals("GET")) {
                int idUsuario = extrairParametroInt(query, "idUsuario");
                List<Endereco> lista = (idUsuario != -1) ? controller.listarPorUsuario(idUsuario) : List.of();
                byte[] bytes = gson.toJson(lista).getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(200, bytes.length);
                ex.getResponseBody().write(bytes);

            } else if (method.equals("POST")) {
                JsonObject obj = JsonParser.parseString(lerCorpo(ex)).getAsJsonObject();
                Endereco novo = controller.cadastrar(montarEndereco(obj));
                byte[] bytes = gson.toJson(novo).getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(201, bytes.length);
                ex.getResponseBody().write(bytes);

            } else if (method.equals("PUT")) {
                int id = extrairIdDoPath(path);
                JsonObject obj = JsonParser.parseString(lerCorpo(ex)).getAsJsonObject();

                if (path.endsWith("/principal")) {
                    controller.definirPrincipal(obj.get("idUsuario").getAsInt(), id);
                } else {
                    Endereco e = montarEndereco(obj);
                    e.setId(id);
                    controller.alterar(e);
                }
                ex.sendResponseHeaders(200, -1);

            } else if (method.equals("DELETE")) {
                int id = extrairIdDoPath(path);
                controller.excluir(id);
                ex.sendResponseHeaders(200, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ex.sendResponseHeaders(500, -1);
        }

        ex.getResponseBody().close();
    }

    private Endereco montarEndereco(JsonObject obj) {
        Endereco e = new Endereco();
        e.setIdUsuario(obj.get("idUsuario").getAsInt());
        e.setApelido(textoOuNulo(obj, "apelido"));
        e.setNomeDestinatario(obj.get("nomeDestinatario").getAsString());
        e.setRua(obj.get("rua").getAsString());
        e.setNumero(obj.get("numero").getAsString());
        e.setBairro(obj.get("bairro").getAsString());
        e.setCidade(obj.get("cidade").getAsString());
        e.setEstado(obj.get("estado").getAsString());
        e.setCep(obj.get("cep").getAsString());
        e.setTelefone(textoOuNulo(obj, "telefone"));
        e.setPrincipal(obj.has("principal") && obj.get("principal").getAsBoolean());
        return e;
    }

    private String textoOuNulo(JsonObject obj, String campo) {
        return (obj.has(campo) && !obj.get(campo).isJsonNull()) ? obj.get(campo).getAsString() : null;
    }

    private int extrairIdDoPath(String path) {
        String resto = path.replace("/api/enderecos/", "");
        return Integer.parseInt(resto.split("/")[0]);
    }

    private int extrairParametroInt(String query, String nome) {
        if (query == null) return -1;
        for (String par : query.split("&")) {
            String[] kv = par.split("=");
            if (kv.length == 2 && kv[0].equals(nome)) {
                try { return Integer.parseInt(kv[1]); } catch (NumberFormatException e) { return -1; }
            }
        }
        return -1;
    }

    private String lerCorpo(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}