CREATE TABLE IF NOT EXISTS produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sabor VARCHAR(255) NOT NULL,
    estoque_inicial INT NOT NULL,
    estoque_atual INT NOT NULL,
    preco_custo DOUBLE NOT NULL,
    preco_custo_unitario DOUBLE,
    preco_venda DECIMAL(10,2),
    horas_producao DOUBLE,
    fonte_agua VARCHAR(255)
);
