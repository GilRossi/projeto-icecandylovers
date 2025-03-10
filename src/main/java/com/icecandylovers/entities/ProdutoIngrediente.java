package com.icecandylovers.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ProdutoIngrediente {

    @EmbeddedId
    private ProdutoIngredienteId id;

    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @MapsId("ingredienteId")
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    private BigDecimal quantidade;


// Getters e Setters


    public ProdutoIngredienteId getId() {
        return id;
    }

    public void setId(ProdutoIngredienteId id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
}

