package com.icecandylovers.services;

import com.icecandylovers.dtos.VendaDTO;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.entities.Venda;
import com.icecandylovers.entities.VendaItem;
import com.icecandylovers.repositories.VendaRepository;
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

    public List<Venda> obterVendasRecentes() {
        return vendaRepository.findTop10ByOrderByDataVendaDesc();
    }

    public BigDecimal calcularVendasHoje() {
        return vendaRepository.findTotalVendasHoje() != null ?
                vendaRepository.findTotalVendasHoje() : BigDecimal.ZERO;
    }

    public List<Venda> listarVendasRecentes() {
        return vendaRepository.findTop5ByOrderByDataVendaDesc();
    }

    public VendaRepository getVendaRepository() {
        return vendaRepository;
    }

    public ProdutoService getProdutoService() {
        return produtoService;
    }

    public void registrarVenda(VendaDTO vendaDTO) {
        Produto produto = produtoService.buscarPorId(vendaDTO.getProdutoId());
        produtoService.decrementarEstoque(vendaDTO.getProdutoId(), vendaDTO.getQuantidade());
        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());

        VendaItem item = new VendaItem();
        item.setProduto(produto);
        item.setQuantidade(vendaDTO.getQuantidade());
        item.setPrecoUnitario(produto.getPrecoVenda());

        venda.setTotal(produto.getPrecoVenda().multiply(
                BigDecimal.valueOf(vendaDTO.getQuantidade())
        ));

        venda.setTotal(produto.getPrecoVenda().multiply(
                BigDecimal.valueOf(vendaDTO.getQuantidade())
        ));
        vendaRepository.save(venda);
    }

}
