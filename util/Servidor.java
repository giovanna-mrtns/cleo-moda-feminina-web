package util;

import com.sun.net.httpserver.*;
import com.google.gson.Gson;
import handler.*;
import java.net.InetSocketAddress;

public class Servidor {
    public static void iniciar() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/usuarios",  new UsuarioHandler());
        server.createContext("/api/modulos",   new ModuloHandler());
        server.createContext("/api/perfis",    new PerfilHandler());
        server.createContext("/api/permissoes",new PermissaoHandler());
        server.createContext("/", new StaticHandler());

        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
    }
}