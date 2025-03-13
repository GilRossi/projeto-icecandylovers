package com.icecandylovers.services;

import com.icecandylovers.dtos.VendaDTO;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.entities.Venda;
import com.icecandylovers.entities.VendaItem;
import com.icecandylovers.exceptions.ResourceNotFoundException;
import com.icecandylovers.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoService produtoService) {
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
    }

    /**
     * Busca um produto pelo ID e lança uma exceção se não for encontrado.
     *
     * @param produtoId ID do produto a ser buscado.
     * @return O produto encontrado.
     * @throws ResourceNotFoundException Se o produto não for encontrado.
     */
    public Produto buscarProdutoPorId(Long produtoId) {
        return produtoService.buscarProdutoPorId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoId));
    }

    /**
     * Obtém as 10 vendas mais recentes.
     *
     * @return Lista das 10 vendas mais recentes.
     */
    public List<Venda> obterVendasRecentes() {
        return vendaRepository.findTop10ByOrderByDataVendaDesc();
    }

    /**
     * Calcula o total de vendas realizadas hoje.
     *
     * @return O total de vendas de hoje.
     */
    public BigDecimal calcularVendasHoje() {
        return vendaRepository.findTotalVendasHoje() != null ?
                vendaRepository.findTotalVendasHoje() : BigDecimal.ZERO;
    }

    /**
     * Obtém as 5 vendas mais recentes.
     *
     * @return Lista das 5 vendas mais recentes.
     */
    public List<Venda> listarVendasRecentes() {
        return vendaRepository.findTop5ByOrderByDataVendaDesc();
    }

    /**
     * Registra uma nova venda no sistema.
     *
     * @param vendaDTO DTO contendo os dados da venda.
     * @throws ResourceNotFoundException Se o produto não for encontrado.
     */
    @Transactional
    public void registrarVenda(VendaDTO vendaDTO) {
        // Busca o produto pelo ID
        Produto produto = buscarProdutoPorId(vendaDTO.produtoId());

        // Decrementa o estoque do produto
        produtoService.decrementarEstoque(vendaDTO.produtoId(), vendaDTO.quantidade());

        // Cria a venda
        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());

        // Cria o item da venda
        VendaItem item = new VendaItem();
        item.setProduto(produto);
        item.setQuantidade(vendaDTO.quantidade());
        item.setPrecoUnitario(produto.getPrecoVenda());

        // Calcula o total da venda
        BigDecimal total = produto.getPrecoVenda().multiply(BigDecimal.valueOf(vendaDTO.quantidade()));
        venda.setTotal(total);

        // Salva a venda no banco de dados
        vendaRepository.save(venda);
    }
}