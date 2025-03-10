package com.icecandylovers.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sabor;
    private BigDecimal precoVenda;
    private int estoqueAtual;
    private Integer estoqueInicial;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoIngrediente> ingredientes = new ArrayList<>();

    private String imagemUrl;

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
    // Getters e Setters


    public Integer getEstoqueInicial() {
        return estoqueInicial;
    }

    public void setEstoqueInicial(Integer estoqueInicial) {
        this.estoqueInicial = estoqueInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public List<ProdutoIngrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<ProdutoIngrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
}