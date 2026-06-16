package util;

import com.sun.net.httpserver.*;
import com.google.gson.Gson;
import handler.*;
import java.net.InetSocketAddress;

public class Servidor {
    public static void iniciar() throws Exception {
        // O Render define a variável PORT automaticamente.
        // Em ambiente local (sem essa variável), usamos 8080.
        String portaEnv = System.getenv("PORT");
        int porta = (portaEnv != null) ? Integer.parseInt(portaEnv) : 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

        server.createContext("/api/usuarios",   new UsuarioHandler());
        server.createContext("/api/modulos",    new ModuloHandler());
        server.createContext("/api/perfis",     new PerfilHandler());
        server.createContext("/api/permissoes", new PermissaoHandler());
        server.createContext("/api/produtos",   new ProdutoHandler());
        server.createContext("/api/pedidos",    new PedidoHandler());
        server.createContext("/api/auth",       new AuthHandler());
        server.createContext("/", new StaticHandler());

        server.start();
        System.out.println("Servidor rodando na porta " + porta);
    }
}
