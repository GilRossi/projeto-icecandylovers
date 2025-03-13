package com.icecandylovers.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class ProdutoIngrediente {

    @EmbeddedId
    private ProdutoIngredienteId id = new ProdutoIngredienteId();

    @ManyToOne
    @MapsId("produtoId")
    private Produto produto;

    @ManyToOne
    @MapsId("ingredienteId")
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

    // Correção do equals() e hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoIngrediente that = (ProdutoIngrediente) o;
        return Objects.equals(id.getProdutoId(), that.id.getProdutoId()) &&
                Objects.equals(id.getIngredienteId(), that.id.getIngredienteId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getProdutoId(), id.getIngredienteId());
    }
}