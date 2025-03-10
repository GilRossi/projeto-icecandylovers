package com.icecandylovers.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProdutoIngredienteId implements Serializable {
    private Long produtoId;
    private Long ingredienteId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoIngredienteId that = (ProdutoIngredienteId) o;
        return Objects.equals(produtoId, that.produtoId) &&
                Objects.equals(ingredienteId, that.ingredienteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produtoId, ingredienteId);
    }
}
