CREATE DATABASE IF NOT EXISTS login_cleo;

USE login_cleo;

CREATE TABLE IF NOT EXISTS usuario (
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(255),
login VARCHAR(45), 
senha VARCHAR(45),
ultimo_acesso DATETIME,
tipo ENUM('Gerente', 'Vendedor', 'Atendente', 'Cliente')
);

CREATE TABLE IF NOT EXISTS perfil (
id_perfil INT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(255),
ativo ENUM('s', 'n')
);

USE login_cleo;
INSERT INTO usuario (
nome, login, senha, tipo
) 
VALUES (
'Admin', 'admin', '1234', 'admin'
);

