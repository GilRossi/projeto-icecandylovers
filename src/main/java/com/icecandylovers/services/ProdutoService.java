package com.icecandylovers.services;

import com.icecandylovers.entities.Ingrediente;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.repositories.IngredienteRepository;
import com.icecandylovers.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.icecandylovers.entities.ProdutoIngrediente;

import java.util.Map;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final IngredienteService ingredienteService; // Adicione esta linha


    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository,
                          IngredienteRepository ingredienteRepository,
                          IngredienteService ingredienteService) {
        this.produtoRepository = produtoRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.ingredienteService = ingredienteService;
    }

    // Operações CRUD básicas
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
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

    // Cálculo de custo do produto
    @Transactional
    public BigDecimal calcularCusto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return produto.getIngredientes().stream()
                .map(pi -> pi.getIngrediente().getCustoPorUnidade().multiply(pi.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Produção de novos produtos
    @Transactional
    public void produzirProduto(Long produtoId, int quantidadeProduzida) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Atualizar estoque de ingredientes
        for (ProdutoIngrediente pi : produto.getIngredientes()) {
            Ingrediente ingrediente = pi.getIngrediente();
            BigDecimal quantidadeNecessaria = pi.getQuantidade().multiply(BigDecimal.valueOf(quantidadeProduzida));

            if (ingrediente.getEstoqueAtual().compareTo(quantidadeNecessaria) < 0) {
                throw new RuntimeException("Estoque insuficiente para o ingrediente: " + ingrediente.getNome());
            }

            // Atualiza estoque do ingrediente
            ingrediente.setEstoqueAtual(ingrediente.getEstoqueAtual().subtract(quantidadeNecessaria));
            ingredienteRepository.save(ingrediente);
        }

        // Atualiza estoque do produto
        produto.setEstoqueAtual(produto.getEstoqueAtual() + quantidadeProduzida);
        produtoRepository.save(produto);
    }

    // Métodos adicionais úteis
    public boolean verificarEstoqueSuficiente(Long produtoId, int quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return produto.getEstoqueAtual() >= quantidade;
    }

    @Transactional
    public void atualizarEstoque(Long produtoId, int quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int novoEstoque = produto.getEstoqueAtual() + quantidade;
        if (novoEstoque < 0) {
            throw new RuntimeException("Estoque não pode ser negativo");
        }

        produto.setEstoqueAtual(novoEstoque);
        produtoRepository.save(produto);
    }

    @Transactional
    public void salvarProdutoComIngredientes(Produto produto, Map<String, String> params) {
        List<ProdutoIngrediente> ingredientes = new ArrayList<>();

        params.keySet().stream()
                .filter(key -> key.startsWith("ingredientes["))
                .forEach(key -> {
                    String index = key.replaceAll("\\D+","");
                    Long ingredienteId = Long.parseLong(params.get("ingredientes["+index+"].ingrediente.id"));
                    BigDecimal quantidade = new BigDecimal(params.get("ingredientes["+index+"].quantidade"));

                    Ingrediente ingrediente = ingredienteService.buscarPorId(ingredienteId)
                            .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));

                    ProdutoIngrediente pi = new ProdutoIngrediente();
                    pi.setProduto(produto);
                    pi.setIngrediente(ingrediente);
                    pi.setQuantidade(quantidade);
                    ingredientes.add(pi);
                });

        produto.setIngredientes(ingredientes);
        produtoRepository.save(produto);
    }
    public int calcularTotalEstoque() {
        return produtoRepository.sumEstoqueAtual();
    }
    public void decrementarEstoque(Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow();
        produto.setEstoqueAtual(produto.getEstoqueAtual() - quantidade);
        produtoRepository.save(produto);
    }
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
}