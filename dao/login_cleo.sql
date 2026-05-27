CREATE DATABASE IF NOT EXISTS login_cleo;
USE login_cleo;

-- --------------------------------------------------------
-- TABELA: usuario
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario   INT AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(255),
    login        VARCHAR(45),
    senha        VARCHAR(45),
    ultimo_acesso DATETIME,
    ativo        ENUM('s', 'n') DEFAULT 's',
    id_perfil    INT
);

-- --------------------------------------------------------
-- TABELA: perfil
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS perfil (
    id_perfil INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    ativo     ENUM('s', 'n') DEFAULT 's'
);

-- --------------------------------------------------------
-- TABELA: modulo
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS modulo (
    id_modulo  INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    descricao  VARCHAR(255),
    ativo      ENUM('s', 'n') DEFAULT 's'
);

-- --------------------------------------------------------
-- TABELA: permissao (tabela intermediária Perfil x Módulo)
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS permissao (
    id_permissao    INT AUTO_INCREMENT PRIMARY KEY,
    id_perfil       INT NOT NULL,
    id_modulo       INT NOT NULL,
    pode_visualizar TINYINT(1) DEFAULT 0,
    pode_editar     TINYINT(1) DEFAULT 0,
    pode_excluir    TINYINT(1) DEFAULT 0,
    UNIQUE KEY uq_perfil_modulo (id_perfil, id_modulo),
    FOREIGN KEY (id_perfil) REFERENCES perfil(id_perfil),
    FOREIGN KEY (id_modulo) REFERENCES modulo(id_modulo)
);

-- --------------------------------------------------------
-- DADOS INICIAIS
-- --------------------------------------------------------
INSERT INTO perfil (nome, ativo) VALUES
    ('Gerente',   's'),
    ('Vendedor',  's'),
    ('Atendente', 's');

INSERT INTO modulo (nome, descricao) VALUES
    ('Financeiro',  'Gestão financeira da loja'),
    ('Estoque',     'Controle de produtos e estoque'),
    ('Atendimento', 'Atendimento ao cliente');

INSERT INTO usuario (nome, login, senha, ativo, id_perfil) VALUES
    ('Admin', 'admin@gmail.com', '1234', 's', 1);
