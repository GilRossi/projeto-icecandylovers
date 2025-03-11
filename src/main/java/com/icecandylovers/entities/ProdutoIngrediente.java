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
    @MapsId("produtoId") // Refere-se ao campo "produtoId" em ProdutoIngredienteId
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @MapsId("ingredienteId") // Refere-se ao campo "ingredienteId" em ProdutoIngredienteId
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

    // Correção do equals() e hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoIngrediente)) return false; // Corrigido para ProdutoIngrediente
        ProdutoIngrediente that = (ProdutoIngrediente) o;
        return Objects.equals(id.getProdutoId(), that.id.getProdutoId()) && // Acesso via getId()
                Objects.equals(id.getIngredienteId(), that.id.getIngredienteId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getProdutoId(), id.getIngredienteId()); // Acesso via getId()
    }
}