-- Criação da tabela Ingrediente
CREATE TABLE IF NOT EXISTS ingrediente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    unidade_medida VARCHAR(255) NOT NULL,
    custo_por_unidade DECIMAL(10, 2) NOT NULL,
    estoque_atual DECIMAL(10, 2) NOT NULL,
    estoque_inicial DECIMAL(10, 2) NOT NULL
);

-- Criação da tabela Produto
CREATE TABLE IF NOT EXISTS produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sabor VARCHAR(255) NOT NULL,
    preco_venda DECIMAL(10, 2) NOT NULL,
    estoque_atual INT NOT NULL,
    estoque_inicial INT,
    preco_custo DOUBLE,
    horas_producao DOUBLE,
    kcal_operadora DOUBLE,
    mj DOUBLE,
    kwh DOUBLE,
    quadrichama DOUBLE,
    rapido DOUBLE,
    semirapido DOUBLE,
    kwh_kg_quadrichama DOUBLE,
    kwh_kg_rapido DOUBLE,
    kwh_kg_semirapido DOUBLE,
    horas_agua DOUBLE,
    horas_gas DOUBLE,
    horas_energia DOUBLE,
    taxa_agua DOUBLE,
    taxa_gas DOUBLE,
    taxa_energia DOUBLE,
    uso_quadrichama BOOLEAN,
    uso_rapido BOOLEAN,
    uso_semirapido BOOLEAN,
    fonte_agua VARCHAR(255),
    quantidade_galoes DOUBLE,
    metros_cubicos_agua DOUBLE
);

-- Criação da tabela ProdutoIngrediente
CREATE TABLE IF NOT EXISTS produto_ingrediente (
    produto_id BIGINT NOT NULL,
    ingrediente_id BIGINT NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (produto_id, ingrediente_id),
    FOREIGN KEY (produto_id) REFERENCES produto(id),
    FOREIGN KEY (ingrediente_id) REFERENCES ingrediente(id)
);

-- Criação da tabela Taxa
CREATE TABLE IF NOT EXISTS taxa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    kwh_kg_quadrichama DOUBLE,
    kwh_kg_rapido DOUBLE,
    kwh_kg_semirapido DOUBLE,
    agua DOUBLE NOT NULL,
    energia DOUBLE NOT NULL,
    taxa_gas DOUBLE NOT NULL,
    kcal DOUBLE,
    mj DOUBLE,
    kwh DOUBLE,
    preco_galao DOUBLE,
    capacidade_galao DOUBLE,
    agua_torneira DOUBLE
);

-- Criação da tabela Venda
CREATE TABLE IF NOT EXISTS venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT,
    quantidade INT NOT NULL,
    data_venda TIMESTAMP NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

-- Criação da tabela VendaItem
CREATE TABLE IF NOT EXISTS venda_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES venda(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);