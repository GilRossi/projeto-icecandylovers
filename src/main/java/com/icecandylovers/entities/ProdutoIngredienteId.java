package com.icecandylovers.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ProdutoIngredienteId implements Serializable {
    private Long produtoId;
    private Long ingredienteId;
}
