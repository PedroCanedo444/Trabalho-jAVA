CREATE TABLE IF NOT EXISTS cliente (
    cpf TEXT PRIMARY KEY,
    nome TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS produto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    preco REAL NOT NULL,
    modelo TEXT NOT NULL,
    tamanho TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS venda (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cpf_cliente TEXT NOT NULL,
    produto_id INTEGER NOT NULL,
    FOREIGN KEY(cpf_cliente) REFERENCES cliente(cpf),
    FOREIGN KEY(produto_id) REFERENCES produto(id)
);
