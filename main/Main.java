package main;

import java.io.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws Exception {
        carregarEnv();
        util.Servidor.iniciar();
    }

    private static void carregarEnv() {
        try {
            for (String linha : Files.readAllLines(Path.of(".env"))) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) continue;
                String[] partes = linha.split("=", 2);
                if (partes.length == 2) {
                    // Define como propriedade do sistema caso não esteja nas env reais
                    if (System.getenv(partes[0]) == null) {
                        System.setProperty(partes[0], partes[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo .env não encontrado — usando variáveis do sistema.");
        }
    }
}