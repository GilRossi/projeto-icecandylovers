package com.icecandylovers.dtos;

import com.icecandylovers.entities.Produto;

import java.util.List;

public record ProdutoDTO(
        Long id,
        String sabor,
        Integer estoqueInicial, // Alterado de int para Integer
        Integer estoqueAtual,    // Alterado de int para Integer
        double precoCusto,
        //double precoCustoUnitario,
        List<ProdutoIngredienteDTO> ingredientes,
        String fonteAgua,
        Double quantidadeGaloes,
        Double metrosCubicosAgua,
        Double horasGas,
        Double kwh,
        Double taxaAgua,
        Double taxaGas,
        Double taxaEnergia,
        Boolean usoQuadrichama,
        Boolean usoRapido,
        Boolean usoSemirapido
) {
    public ProdutoDTO {
        if (ingredientes == null) {
            ingredientes = List.of();
        }
        if (estoqueInicial == null) {
            estoqueInicial = 0;
        }
        if (estoqueAtual == null) {
            estoqueAtual = 0;
        }
    }
    private ProdutoDTO convertToProdutoDTO(Produto produto) {
        List<ProdutoIngredienteDTO> ingredientesDTO = produto.getIngredientes().stream()
                .map(pi -> new ProdutoIngredienteDTO(pi.getIngrediente().getId(), pi.getQuantidade().doubleValue()))
                .toList();

        return new ProdutoDTO(
                produto.getId(),
                produto.getSabor(),
                produto.getEstoqueInicial(),
                produto.getEstoqueAtual(),
                produto.getPrecoCusto(),
                ingredientesDTO,
                produto.getFonteAgua(),
                produto.getQuantidadeGaloes(),
                produto.getMetrosCubicosAgua(),
                produto.getHorasGas(),
                produto.getKwh(),
                produto.getTaxaAgua(),
                produto.getTaxaGas(),
                produto.getTaxaEnergia(),
                produto.getUsoQuadrichama(),
                produto.getUsoRapido(),
                produto.getUsoSemirapido()
        );
    }
}
