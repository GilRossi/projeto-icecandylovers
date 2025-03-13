package com.icecandylovers.services;

import com.icecandylovers.entities.Produto;
import com.icecandylovers.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeladinhoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public void atualizar(Produto produto) {
        produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}
