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
-- 5. DADOS INICIAIS
--    Só insere se as tabelas estiverem vazias
-- -------------------------------------------------------------
INSERT INTO perfil (nome)
SELECT * FROM (VALUES ('Gerente'), ('Vendedor'), ('Atendente'), ('Cliente')) AS v(nome)
WHERE NOT EXISTS (SELECT 1 FROM perfil);

INSERT INTO usuario (nome, login, senha, id_perfil)
SELECT 'Admin', 'admin@gmail.com', '1234', 1
WHERE NOT EXISTS (SELECT 1 FROM usuario);
