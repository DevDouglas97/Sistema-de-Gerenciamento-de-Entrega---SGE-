-- ============================================================
-- SGE - Sistema de Gerenciamento de Entregas
-- Script de criação do banco de dados (MySQL 5.7+/8.0)
--
-- Uso:
--   mysql -u root -p < database/schema.sql
--
-- O banco "sge" é criado caso não exista.
-- ============================================================

CREATE DATABASE IF NOT EXISTS sge
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sge;

-- Ordem de remoção respeita as chaves estrangeiras.
DROP TABLE IF EXISTS rotas;
DROP TABLE IF EXISTS itens_expedicao;
DROP TABLE IF EXISTS expedicao;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS enderecos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS clientes;

-- ------------------------------------------------------------
-- Clientes
-- ------------------------------------------------------------
CREATE TABLE clientes (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nome_empresa VARCHAR(150) NOT NULL,
    cnpj         VARCHAR(20),
    telefone     VARCHAR(30),
    email        VARCHAR(120),
    UNIQUE KEY uk_clientes_nome_empresa (nome_empresa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Usuários
-- A coluna senha armazena o hash PBKDF2 gerado por Util.SenhaUtil.
-- ------------------------------------------------------------
CREATE TABLE usuarios (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nome   VARCHAR(120) NOT NULL,
    cpf    VARCHAR(20),
    login  VARCHAR(60) NOT NULL,
    senha  VARCHAR(255) NOT NULL,
    perfil VARCHAR(40) NOT NULL,
    ativo  BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uk_usuarios_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Endereços
-- ------------------------------------------------------------
CREATE TABLE enderecos (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    descricao  VARCHAR(80),
    endereco   VARCHAR(200) NOT NULL,
    bairro     VARCHAR(100),
    cidade_uf  VARCHAR(100),
    CONSTRAINT fk_enderecos_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Produtos
-- ------------------------------------------------------------
CREATE TABLE produtos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id   INT NOT NULL,
    nome_produto VARCHAR(120) NOT NULL,
    quantidade   INT NOT NULL DEFAULT 0,
    unidade      VARCHAR(10),
    categoria    VARCHAR(60),
    CONSTRAINT fk_produtos_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Expedições (pedidos)
-- ------------------------------------------------------------
CREATE TABLE expedicao (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id   INT,
    motorista_id INT,
    status       VARCHAR(40) NOT NULL DEFAULT 'Aguardando',
    CONSTRAINT fk_expedicao_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes (id) ON DELETE SET NULL,
    CONSTRAINT fk_expedicao_motorista
        FOREIGN KEY (motorista_id) REFERENCES usuarios (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Itens da expedição
-- ------------------------------------------------------------
CREATE TABLE itens_expedicao (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    expedicao_id INT NOT NULL,
    produto_id   INT NOT NULL,
    quantidade   INT NOT NULL,
    CONSTRAINT fk_itens_expedicao
        FOREIGN KEY (expedicao_id) REFERENCES expedicao (id) ON DELETE CASCADE,
    CONSTRAINT fk_itens_produto
        FOREIGN KEY (produto_id) REFERENCES produtos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Rotas de entrega
-- ------------------------------------------------------------
CREATE TABLE rotas (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    expedicao_id INT NOT NULL,
    endereco_id  INT NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'Pendente',
    CONSTRAINT fk_rotas_expedicao
        FOREIGN KEY (expedicao_id) REFERENCES expedicao (id) ON DELETE CASCADE,
    CONSTRAINT fk_rotas_endereco
        FOREIGN KEY (endereco_id) REFERENCES enderecos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Usuário inicial
-- Login: admin
-- Senha: admin123  (TROQUE após o primeiro acesso)
-- O hash abaixo foi gerado com Util.SenhaUtil (PBKDF2WithHmacSHA256).
-- ------------------------------------------------------------
INSERT IGNORE INTO usuarios (nome, cpf, login, senha, perfil, ativo) VALUES
('Administrador', '00000000000', 'admin',
 'pbkdf2$sha256$120000$LOSuiPAE6FTEws1HTMMtuw$w6f3otH2AecIoPv95Dp1AiEoD068yoQbAwj4uvng3O4',
 'Gerente', TRUE);

-- ------------------------------------------------------------
-- Dados de exemplo (opcional). Descomente para testar rapidamente.
-- ------------------------------------------------------------
-- INSERT IGNORE INTO clientes (nome_empresa, cnpj, telefone, email) VALUES
-- ('Empresa Exemplo Ltda', '12.345.678/0001-90', '(51) 99999-0000', 'contato@exemplo.com');
--
-- INSERT INTO enderecos (cliente_id, descricao, endereco, bairro, cidade_uf)
-- SELECT id, 'Sede', 'Rua das Flores, 123', 'Centro', 'Porto Alegre/RS'
-- FROM clientes WHERE nome_empresa = 'Empresa Exemplo Ltda';
--
-- INSERT INTO produtos (cliente_id, nome_produto, quantidade, unidade, categoria)
-- SELECT id, 'Caixa Padrão', 100, 'CX', 'Geral'
-- FROM clientes WHERE nome_empresa = 'Empresa Exemplo Ltda';
