-- =============================================================
--  schema.sql — Sistema Login Cleo
--  Banco: PostgreSQL
--  Como usar: psql -U seu_usuario -d login_cleo -f schema.sql
-- =============================================================

-- -------------------------------------------------------------
-- 1. PERFIL
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS perfil (
    id    SERIAL PRIMARY KEY,
    nome  VARCHAR(100) NOT NULL,
    ativo CHAR(1) DEFAULT 's'
);

-- -------------------------------------------------------------
-- 2. USUARIO
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id            SERIAL PRIMARY KEY,
    nome          VARCHAR(150) NOT NULL,
    login         VARCHAR(100) NOT NULL UNIQUE,
    senha         VARCHAR(255) NOT NULL,
    ultimo_acesso TIMESTAMP,
    ativo         CHAR(1) DEFAULT 's',
    id_perfil     INT,
    FOREIGN KEY (id_perfil) REFERENCES perfil(id)
);

-- -------------------------------------------------------------
-- 3. MODULO
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS modulo (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    ativo     CHAR(1) DEFAULT 's'
);

-- -------------------------------------------------------------
-- 4. PERMISSAO
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS permissao (
    id              SERIAL PRIMARY KEY,
    id_perfil       INT NOT NULL,
    id_modulo       INT NOT NULL,
    pode_visualizar BOOLEAN DEFAULT FALSE,
    pode_editar     BOOLEAN DEFAULT FALSE,
    pode_excluir    BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_perfil) REFERENCES perfil(id),
    FOREIGN KEY (id_modulo) REFERENCES modulo(id)
);

-- -------------------------------------------------------------
-- 5. PRODUTO
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS produto (
    id         SERIAL PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    descricao  TEXT,
    preco      NUMERIC(10, 2) NOT NULL,
    estoque    INT DEFAULT 0,
    categoria  VARCHAR(100),
    imagem_url VARCHAR(500),
    ativo      CHAR(1) DEFAULT 's'
);

-- -------------------------------------------------------------
-- 6. PEDIDO
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pedido (
    id          SERIAL PRIMARY KEY,
    id_usuario  INT NOT NULL,
    data_pedido TIMESTAMP DEFAULT NOW(),
    status      VARCHAR(20) DEFAULT 'pendente',
    total       NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- -------------------------------------------------------------
-- 7. ITEM_PEDIDO
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS item_pedido (
    id              SERIAL PRIMARY KEY,
    id_pedido       INT NOT NULL,
    id_produto      INT NOT NULL,
    quantidade      INT NOT NULL,
    preco_unitario  NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (id_pedido)  REFERENCES pedido(id),
    FOREIGN KEY (id_produto) REFERENCES produto(id)
);

-- -------------------------------------------------------------
-- 8. DADOS INICIAIS
--    Só insere se as tabelas estiverem vazias
-- -------------------------------------------------------------
INSERT INTO perfil (nome)
SELECT * FROM (VALUES ('Gerente'), ('Vendedor'), ('Atendente'), ('Cliente')) AS v(nome)
WHERE NOT EXISTS (SELECT 1 FROM perfil);

INSERT INTO usuario (nome, login, senha, id_perfil)
SELECT 'Admin', 'admin@gmail.com', '1234', 1
WHERE NOT EXISTS (SELECT 1 FROM usuario);
