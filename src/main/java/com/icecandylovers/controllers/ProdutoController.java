package com.icecandylovers.controllers;

import com.icecandylovers.dtos.ProdutoDTO;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.exceptions.ResourceNotFoundException;
import com.icecandylovers.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/criar")
    public ResponseEntity<Produto> criarProduto(@RequestBody ProdutoDTO produtoDTO) {
        Produto produto = produtoService.criarProdutoAPartirDoDTO(produtoDTO);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id)
                .map(produto -> ResponseEntity.ok(produtoService.converterParaDTO(produto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/produzir")
    public ResponseEntity<String> produzirProduto(@PathVariable Long id, @RequestParam int quantidade) {
        produtoService.produzirProduto(id, quantidade);
        return ResponseEntity.ok("Produção registrada");
    }

    @GetMapping("/{id}/custo")
    public ResponseEntity<BigDecimal> getCustoProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.calcularCusto(id));
    }
    @RestControllerAdvice
    public class CustomExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGenericException(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno: " + ex.getMessage());
        }
    }
}