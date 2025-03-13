-- Inserção de dados na tabela Ingrediente
INSERT INTO ingrediente (nome, unidade_medida, custo_por_unidade, estoque_atual, estoque_inicial) VALUES
('Leite Condensado', 'Litro', 10.00, 50.00, 50.00),
('Chocolate', 'Quilo', 20.00, 30.00, 30.00),
('Morango', 'Quilo', 15.00, 40.00, 40.00);

-- Inserção de dados na tabela Produto
INSERT INTO produto (sabor, preco_venda, estoque_atual, estoque_inicial, preco_custo, horas_producao) VALUES
('Ninho com Nutella', 5.00, 100, 100, 3.00, 2.5),
('Morango com Chocolate', 6.00, 80, 80, 4.00, 3.0);

-- Inserção de dados na tabela ProdutoIngrediente
INSERT INTO produto_ingrediente (produto_id, ingrediente_id, quantidade) VALUES
(1, 1, 1.00), -- Ninho com Nutella usa Leite Condensado
(1, 2, 0.50), -- Ninho com Nutella usa Chocolate
(2, 1, 1.00), -- Morango com Chocolate usa Leite Condensado
(2, 3, 0.75); -- Morango com Chocolate usa Morango

-- Inserção de dados na tabela Taxa
INSERT INTO taxa (agua, energia, taxa_gas, preco_galao, capacidade_galao, agua_torneira) VALUES
(5.00, 7.24, 11.50, 17.00, 20.00, 0.10);

-- Inserção de dados na tabela Venda
INSERT INTO venda (produto_id, quantidade, data_venda, total) VALUES
(1, 2, '2025-03-12 10:00:00', 10.00),
(2, 3, '2025-03-12 11:00:00', 18.00);

-- Inserção de dados na tabela VendaItem
INSERT INTO venda_item (venda_id, produto_id, quantidade, preco_unitario) VALUES
(1, 1, 2, 5.00),
(2, 2, 3, 6.00);

ALTER TABLE produto ALTER COLUMN preco_venda DROP NOT NULL;
