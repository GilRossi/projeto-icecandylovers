package com.icecandylovers.controllers;

import com.icecandylovers.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/{id}/produzir")
    public ResponseEntity<String> produzirProduto(
            @PathVariable Long id,
            @RequestParam int quantidade
    ) {
        produtoService.produzirProduto(id, quantidade);
        return ResponseEntity.ok("Produção registrada");
    }

    @GetMapping("/{id}/custo")
    public ResponseEntity<BigDecimal> getCustoProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.calcularCusto(id));
    }
}
