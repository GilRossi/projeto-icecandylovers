package com.icecandylovers.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProdutoIngredienteId implements Serializable {
    private Long produtoId;
    private Long ingredienteId;

    // Construtor padrão OBRIGATÓRIO
    public ProdutoIngredienteId() {}

    // Getters e Setters
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public Long getIngredienteId() { return ingredienteId; }
    public void setIngredienteId(Long ingredienteId) { this.ingredienteId = ingredienteId; }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoIngredienteId)) return false;
        ProdutoIngredienteId that = (ProdutoIngredienteId) o;
        return Objects.equals(produtoId, that.produtoId) &&
                Objects.equals(ingredienteId, that.ingredienteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produtoId, ingredienteId);
    }
}