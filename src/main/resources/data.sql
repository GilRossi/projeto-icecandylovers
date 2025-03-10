INSERT INTO ingrediente (nome, unidade_medida, custo_por_unidade, estoque_atual)
VALUES
('Leite Ninho', 'GRAMAS', 0.15, 10000.00),
('Nutella', 'GRAMAS', 0.30, 5000.00);

INSERT INTO produto (sabor, preco_venda, estoque_atual)
VALUES ('Ninho com Nutella', 5.00, 100);

UPDATE produto SET imagem_url = 'img/geladinho-ninho.jpg' WHERE id = 1;