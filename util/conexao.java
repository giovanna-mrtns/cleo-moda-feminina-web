package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    private static String env(String chave, String padrao) {
        String val = System.getenv(chave);
        if (val != null) return val;
        val = System.getProperty(chave);
        if (val != null) return val;
        return padrao;
    }

    private static final String MODO       = env("DB_MODO", "h2");
    private static final String MYSQL_URL  = env("DB_URL",  "jdbc:mysql://localhost:3306/login_cleo?useSSL=false&serverTimezone=America/Sao_Paulo");
    private static final String MYSQL_USER = env("DB_USER", "root");
    private static final String MYSQL_PASS = env("DB_PASS", "");

    // Config H2 (embutido)
    private static final String H2_URL  = "jdbc:h2:./data/sistema;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    private static final String H2_USER = "sa";
    private static final String H2_PASS = "";

    public static Connection getConexao() throws Exception {
        if (MODO.equals("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
        } else {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASS);
            inicializarH2(conn);
            return conn;
        }
    }

    // Cria as tabelas automaticamente se ainda não existirem
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
            conn.createStatement().executeUpdate
            ("INSERT INTO perfil (nome) VALUES ('Administrador')");
            conn.createStatement().executeUpdate("INSERT INTO perfil (nome) VALUES ('Operador')");
            conn.createStatement().executeUpdate("""
                INSERT INTO usuario (nome, login, senha, id_perfil)
                VALUES ('Admin', 'admin@gmail.com', '1234', 1)
            """);
        }
    }
}