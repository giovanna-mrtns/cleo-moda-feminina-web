package util;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.URI;
import java.nio.file.*;

public class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange ex) throws IOException {
        URI uri  = ex.getRequestURI();
        String caminho = uri.getPath().equals("/") ? "/index.html" : uri.getPath();
        File arquivo = new File("frontend" + caminho);

        if (!arquivo.exists()) {
            ex.sendResponseHeaders(404, -1); return;
        }

        String tipo = caminho.endsWith(".html") ? "text/html; charset=UTF-8"
                    : caminho.endsWith(".css")  ? "text/css"
                    : caminho.endsWith(".js")   ? "application/javascript"
                    : caminho.endsWith(".png")  ? "image/png"
                    : caminho.endsWith(".jpg")  ? "image/jpeg"
                    : caminho.endsWith(".ttf")  ? "font/ttf"
                    : "application/octet-stream";

        byte[] bytes = Files.readAllBytes(arquivo.toPath());
        ex.getResponseHeaders().add("Content-Type", tipo);
        ex.sendResponseHeaders(200, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }
}