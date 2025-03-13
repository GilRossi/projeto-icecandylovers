package com.icecandylovers.dtos;

import java.util.List;

public record ProdutoDTO(
        Long id,
        String sabor,
        Integer estoqueInicial, // Alterado de int para Integer
        Integer estoqueAtual,    // Alterado de int para Integer
        double precoCusto,
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
}
