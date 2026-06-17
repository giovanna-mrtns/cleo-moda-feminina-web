package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    // -----------------------------------------------------------------
    // Lê variável de ambiente, depois propriedade do sistema, depois usa
    // o valor padrão — mesma lógica de antes.
    // -----------------------------------------------------------------
    private static String env(String chave, String padrao) {
        String val = System.getenv(chave);
        if (val != null) return val;
        val = System.getProperty(chave);
        if (val != null) return val;
        return padrao;
    }

    // Valores lidos UMA vez na inicialização da classe
    private static final String MODO = env("DB_MODO", "h2"); // "h2" | "mysql" | "postgres"

    // --- PostgreSQL ---------------------------------------------------
    private static final String PG_URL  = env("DB_URL",  "jdbc:postgresql://localhost:5432/login_cleo");
    private static final String PG_USER = env("DB_USER", "postgres");
    private static final String PG_PASS = env("DB_PASS", "");

    // --- MySQL --------------------------------------------------------
    private static final String MYSQL_URL  = env("DB_URL",  "jdbc:mysql://localhost:3306/login_cleo?useSSL=false&serverTimezone=America/Sao_Paulo");
    private static final String MYSQL_USER = env("DB_USER", "root");
    private static final String MYSQL_PASS = env("DB_PASS", "");

    // --- H2 (desenvolvimento local) -----------------------------------
    private static final String H2_URL  = "jdbc:h2:./data/sistema;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    private static final String H2_USER = "sa";
    private static final String H2_PASS = "";

    // -----------------------------------------------------------------
    // DIAGNÓSTICO: imprime no log, no momento em que o servidor sobe,
    // qual banco está realmente sendo usado. Aparece junto do log de
    // start, sem precisar pegar o erro em tempo real.
    // -----------------------------------------------------------------
    // -----------------------------------------------------------------
    // DIAGNÓSTICO: chamado explicitamente no Servidor.iniciar(), pra
    // garantir que rode no momento do start (e não só na primeira
    // requisição, que é quando a classe seria carregada de forma
    // automática se isso ficasse num bloco static).
    // -----------------------------------------------------------------
    public static void testarConexao() {
        System.out.println("=== Conexao: DB_MODO lido como -> [" + MODO + "] ===");
        if (MODO.equals("postgres")) {
            System.out.println("=== Conexao: usando PostgreSQL, URL = " + PG_URL + " ===");
        } else if (MODO.equals("mysql")) {
            System.out.println("=== Conexao: usando MySQL, URL = " + MYSQL_URL + " ===");
        } else {
            System.out.println("=== Conexao: ATENÇÃO - usando H2 local (não é o Postgres do DBeaver!) ===");
        }

        try (Connection testeConn = getConexao()) {
            System.out.println("=== Conexao: TESTE DE CONEXÃO COM O BANCO -> SUCESSO ===");
        } catch (Exception e) {
            System.out.println("=== Conexao: TESTE DE CONEXÃO COM O BANCO -> FALHOU ===");
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------------
    // Ponto central de conexão — escolhe o banco pelo DB_MODO
    // -----------------------------------------------------------------
    public static Connection getConexao() throws Exception {
        switch (MODO) {
            case "postgres" -> {
                Class.forName("org.postgresql.Driver");
                return DriverManager.getConnection(PG_URL, PG_USER, PG_PASS);
            }
            case "mysql" -> {
                Class.forName("com.mysql.jdbc.Driver");
                return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            }
            default -> {
                // "h2" — banco em arquivo local para desenvolvimento
                Class.forName("org.h2.Driver");
                Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASS);
                inicializarH2(conn);
                return conn;
            }
        }
    }

    // -----------------------------------------------------------------
    // Inicialização automática do H2 (só roda em modo "h2")
    // No PostgreSQL as tabelas são criadas pelo schema.sql antes de subir
    // -----------------------------------------------------------------
    private static boolean inicializado = false;

    private static synchronized void inicializarH2(Connection conn) throws Exception {
        if (inicializado) return;
        inicializado = true;

        conn.createStatement().executeUpdate("""
            CREATE TABLE IF NOT EXISTS perfil (
                id    INT AUTO_INCREMENT PRIMARY KEY,
                nome  VARCHAR(100) NOT NULL,
                ativo CHAR(1) DEFAULT 's'
            )
        """);

        conn.createStatement().executeUpdate("""
            CREATE TABLE IF NOT EXISTS usuario (
                id            INT AUTO_INCREMENT PRIMARY KEY,
                nome          VARCHAR(150) NOT NULL,
                login         VARCHAR(100) NOT NULL UNIQUE,
                senha         VARCHAR(255) NOT NULL,
                ultimo_acesso TIMESTAMP,
                ativo         CHAR(1) DEFAULT 's',
                id_perfil     INT,
                FOREIGN KEY (id_perfil) REFERENCES perfil(id)
            )
        """);

        conn.createStatement().executeUpdate("""
            CREATE TABLE IF NOT EXISTS modulo (
                id        INT AUTO_INCREMENT PRIMARY KEY,
                nome      VARCHAR(100) NOT NULL,
                descricao VARCHAR(255),
                ativo     CHAR(1) DEFAULT 's'
            )
        """);

        conn.createStatement().executeUpdate("""
            CREATE TABLE IF NOT EXISTS permissao (
                id              INT AUTO_INCREMENT PRIMARY KEY,
                id_perfil       INT NOT NULL,
                id_modulo       INT NOT NULL,
                pode_visualizar BOOLEAN DEFAULT FALSE,
                pode_editar     BOOLEAN DEFAULT FALSE,
                pode_excluir    BOOLEAN DEFAULT FALSE,
                FOREIGN KEY (id_perfil) REFERENCES perfil(id),
                FOREIGN KEY (id_modulo) REFERENCES modulo(id)
            )
        """);

        // Dados iniciais — só insere se a tabela estiver vazia
        var rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM perfil");
        rs.next();
        if (rs.getInt(1) == 0) {
            conn.createStatement().executeUpdate("INSERT INTO perfil (nome) VALUES ('Gerente')");
            conn.createStatement().executeUpdate("INSERT INTO perfil (nome) VALUES ('Vendedor')");
            conn.createStatement().executeUpdate("INSERT INTO perfil (nome) VALUES ('Atendente')");
            conn.createStatement().executeUpdate("INSERT INTO perfil (nome) VALUES ('Cliente')");

            conn.createStatement().executeUpdate("""
                INSERT INTO usuario (nome, login, senha, id_perfil)
                VALUES ('Admin', 'admin@gmail.com', '1234', 1)
            """);
        }
    }
}