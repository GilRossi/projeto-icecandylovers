INSERT INTO produto (sabor, estoque_inicial, estoque_atual, preco_custo, preco_venda, horas_producao, fonte_agua)
VALUES ('Chocolate', 100, 100, 150.00, 200.00, 2.5, 'Poço');

INSERT INTO produto (sabor, estoque_inicial, estoque_atual, preco_custo, preco_venda, horas_producao, fonte_agua)
VALUES ('Morango', 200, 200, 180.00, 220.00, 3.0, 'Encanada');

ALTER TABLE produto ADD COLUMN custo_agua DOUBLE;

