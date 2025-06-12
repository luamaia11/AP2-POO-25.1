-- Script de Criação do Banco para Sistema de Avaliação de Filmes

DROP SCHEMA IF EXISTS avaliacao_filmes;
CREATE SCHEMA IF NOT EXISTS avaliacao_filmes DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE avaliacao_filmes;

-- Tabela de usuários
CREATE TABLE IF NOT EXISTS usuarios (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  senha VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

-- Tabela de filmes
CREATE TABLE IF NOT EXISTS filmes (
  id INT NOT NULL AUTO_INCREMENT,
  titulo VARCHAR(200) NOT NULL,
  ano INT NOT NULL,
  genero VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

-- Tabela de avaliações
CREATE TABLE IF NOT EXISTS avaliacoes (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  filme_id INT NOT NULL,
  nota INT NOT NULL CHECK (nota BETWEEN 0 AND 10),
  comentario TEXT,
  PRIMARY KEY (id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (filme_id) REFERENCES filmes(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;