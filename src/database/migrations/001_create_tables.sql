-- Cria o banco de dados "real_estate" (imobiliária)
CREATE DATABASE real_estate_ana;
-- Seleciona o banco de dados
USE real_estate_ana;

DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Houses;
DROP TABLE IF EXISTS Transactions;

-- Tabela de Usuários (Users)
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,             -- ID único do usuário
    name VARCHAR(100) NOT NULL,                    -- Nome do usuário
    email VARCHAR(100) NOT NULL UNIQUE,            -- Email do usuário (único)
    password VARCHAR(255) NOT NULL,                -- Senha criptografada
    cpf CHAR(11) NOT NULL UNIQUE,                  -- Cpf sem caracteres especiais (.-)
    type ENUM('Realtor', 'Buyer') NOT NULL         -- Tipo de usuário (Corretor ou Comprador)
);

-- Tabela de Casas (Houses)
CREATE TABLE Houses (
    id INT AUTO_INCREMENT PRIMARY KEY,             -- ID único da casa
    address VARCHAR(255) NOT NULL,                 -- Endereço da casa
    description VARCHAR(550) NOT NULL,             -- Descrição da casa
    price DECIMAL(10, 2) NOT NULL,                 -- Preço da casa
    size DECIMAL(10, 2) NOT NULL,                  -- Tamanho da casa em m²
    realtor_id INT NOT NULL,                       -- ID do corretor responsável
    FOREIGN KEY (realtor_id) REFERENCES Users(id)  -- Chave estrangeira para o corretor
);

-- Tabela de Transações (Transactions)
CREATE TABLE Transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,             -- ID único da transação
    date DATE NOT NULL,                            -- Data da transação
    amount DECIMAL(10, 2) NOT NULL,                -- Valor da transação
    buyer_id INT NOT NULL,                         -- ID do comprador
    realtor_id INT NOT NULL,                       -- ID do corretor
    house_id INT NOT NULL,                         -- ID da casa vendida
    FOREIGN KEY (buyer_id) REFERENCES Users(id),   -- Chave estrangeira para o comprador
    FOREIGN KEY (realtor_id) REFERENCES Users(id), -- Chave estrangeira para o corretor
    FOREIGN KEY (house_id) REFERENCES Houses(id)   -- Chave estrangeira para a casa
);