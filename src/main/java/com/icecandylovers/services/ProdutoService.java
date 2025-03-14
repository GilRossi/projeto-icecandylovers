package com.icecandylovers.services;

import com.icecandylovers.dtos.ProdutoDTO;
import com.icecandylovers.dtos.ProdutoIngredienteDTO;
import com.icecandylovers.entities.Ingrediente;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.entities.ProdutoIngrediente;
import com.icecandylovers.exceptions.ResourceNotFoundException;
import com.icecandylovers.repositories.IngredienteRepository;
import com.icecandylovers.repositories.ProdutoIngredienteRepository;
import com.icecandylovers.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final ProdutoIngredienteRepository produtoIngredienteRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository,
                          IngredienteRepository ingredienteRepository,
                          ProdutoIngredienteRepository produtoIngredienteRepository) {
        this.produtoRepository = produtoRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.produtoIngredienteRepository = produtoIngredienteRepository;
    }

    @Transactional
    public Produto salvarProduto(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setSabor(produtoDTO.sabor());
        produto.setEstoqueInicial(produtoDTO.estoqueInicial());
        produto.setEstoqueAtual(produtoDTO.estoqueAtual());
        produto.setPrecoCusto(produtoDTO.precoCusto());

        List<ProdutoIngrediente> ingredientes = produtoDTO.ingredientes().stream()
                .map(dto -> {
                    ProdutoIngrediente ingrediente = new ProdutoIngrediente();
                    ingrediente.setProduto(produto);
                    ingrediente.setIngrediente(ingredienteRepository.findById(dto.ingredienteId())
                            .orElseThrow(() -> new IllegalArgumentException("Ingrediente não encontrado")));
                    ingrediente.setQuantidade(BigDecimal.valueOf(dto.quantidade())); // Conversão correta
                    return ingrediente;
                })
                .collect(Collectors.toList());

        produto.getIngredientes().clear();
        produto.getIngredientes().addAll(ingredientes);

        return produtoRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    public ProdutoDTO converterParaDTO(Produto produto) {
        List<ProdutoIngredienteDTO> ingredientesDTO = produto.getIngredientes().stream()
                .map(this::converterIngredienteParaDTO)
                .collect(Collectors.toList());

        return new ProdutoDTO(
                produto.getId(),
                produto.getSabor(),
                produto.getEstoqueInicial(),
                produto.getEstoqueAtual(),
                produto.getPrecoCusto() != null ? produto.getPrecoCusto() : 0.0,
                //produto.getPrecoCustoUnitario() != null ? produto.getPrecoCustoUnitario() : 0.0,
                ingredientesDTO,
                produto.getFonteAgua(),
                produto.getQuantidadeGaloes(),
                produto.getMetrosCubicosAgua(),
                produto.getHorasGas(),
                produto.getKwh(),
                produto.getTaxaAgua(),
                produto.getTaxaGas(),
                produto.getTaxaEnergia(),
                produto.getUsoQuadrichama() != null ? produto.getUsoQuadrichama() : false,
                produto.getUsoRapido() != null ? produto.getUsoRapido() : false,
                produto.getUsoSemirapido() != null ? produto.getUsoSemirapido() : false
        );
    }


    private ProdutoIngredienteDTO converterIngredienteParaDTO(ProdutoIngrediente produtoIngrediente) {
        return new ProdutoIngredienteDTO(
                produtoIngrediente.getIngrediente().getId(),
                produtoIngrediente.getQuantidade().doubleValue()
        );
    }

    public Produto criarProdutoAPartirDoDTO(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setSabor(produtoDTO.sabor());
        produto.setEstoqueInicial(produtoDTO.estoqueInicial());
        produto.setEstoqueAtual(produtoDTO.estoqueAtual());
        produto.setPrecoCusto(produtoDTO.precoCusto());
        //produto.setPrecoCustoUnitario(produtoDTO.precoCustoUnitario());

        produto.setFonteAgua(produtoDTO.fonteAgua());
        produto.setQuantidadeGaloes(produtoDTO.quantidadeGaloes());
        produto.setMetrosCubicosAgua(produtoDTO.metrosCubicosAgua());
        produto.setHorasGas(produtoDTO.horasGas());
        produto.setKwh(produtoDTO.kwh());
        produto.setTaxaAgua(produtoDTO.taxaAgua());
        produto.setTaxaGas(produtoDTO.taxaGas());
        produto.setTaxaEnergia(produtoDTO.taxaEnergia());
        produto.setUsoQuadrichama(produtoDTO.usoQuadrichama());
        produto.setUsoRapido(produtoDTO.usoRapido());
        produto.setUsoSemirapido(produtoDTO.usoSemirapido());

        produto.setPrecoVenda(null); // Mantém o preço de venda indefinido até a venda acontecer

        return produto;
    }



    private void salvarIngredientesDoProduto(Produto produto, List<ProdutoIngredienteDTO> ingredientesDTO) {
        List<ProdutoIngrediente> ingredientes = ingredientesDTO.stream()
                .map(ingredienteDTO -> criarProdutoIngrediente(produto, ingredienteDTO))
                .collect(Collectors.toList());

        produtoIngredienteRepository.saveAll(ingredientes);
        produto.setIngredientes(ingredientes);
    }

    private ProdutoIngrediente criarProdutoIngrediente(Produto produto, ProdutoIngredienteDTO ingredienteDTO) {
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteDTO.ingredienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado"));

        ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente();
        produtoIngrediente.setProduto(produto);
        produtoIngrediente.setIngrediente(ingrediente);
        produtoIngrediente.setQuantidade(BigDecimal.valueOf(ingredienteDTO.quantidade()));
        return produtoIngrediente;
    }

    @Transactional
    public BigDecimal calcularCusto(Long produtoId) {
        Produto produto = buscarProdutoPorId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return produto.getIngredientes().stream()
                .map(produtoIngrediente -> produtoIngrediente.getIngrediente()
                        .getCustoPorUnidade()
                        .multiply(produtoIngrediente.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void produzirProduto(Long produtoId, int quantidadeProduzida) {
        Produto produto = buscarProdutoPorId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        produto.getIngredientes().forEach(produtoIngrediente -> {
            Ingrediente ingrediente = produtoIngrediente.getIngrediente();
            BigDecimal quantidadeNecessaria = produtoIngrediente.getQuantidade().multiply(BigDecimal.valueOf(quantidadeProduzida));

            if (ingrediente.getEstoqueAtual().compareTo(quantidadeNecessaria) < 0) {
                throw new ResourceNotFoundException("Estoque insuficiente para o ingrediente: " + ingrediente.getNome());
            }

            ingrediente.setEstoqueAtual(ingrediente.getEstoqueAtual().subtract(quantidadeNecessaria));
            ingredienteRepository.save(ingrediente);
        });

        produto.setEstoqueAtual(produto.getEstoqueAtual() + quantidadeProduzida);
        produtoRepository.save(produto);
    }

    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public void excluirProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    public boolean verificarEstoqueSuficiente(Long produtoId, int quantidade) {
        Produto produto = buscarProdutoPorId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return produto.getEstoqueAtual() >= quantidade;
    }

    @Transactional
    public void atualizarEstoque(Long produtoId, int quantidade) {
        Produto produto = buscarProdutoPorId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        int novoEstoque = produto.getEstoqueAtual() + quantidade;
        if (novoEstoque < 0) {
            throw new ResourceNotFoundException("Estoque não pode ser negativo");
        }

        produto.setEstoqueAtual(novoEstoque);
        produtoRepository.save(produto);
    }

    public int calcularTotalEstoque() {
        return produtoRepository.sumEstoqueAtual();
    }

    public void decrementarEstoque(Long produtoId, int quantidade) {
        Produto produto = buscarProdutoPorId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        produto.setEstoqueAtual(produto.getEstoqueAtual() - quantidade);
        produtoRepository.save(produto);
    }
}