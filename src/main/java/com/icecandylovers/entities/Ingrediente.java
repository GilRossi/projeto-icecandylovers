package com.icecandylovers.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String unidadeMedida;

    @Column(precision = 10, scale = 2)
    private BigDecimal custoPorUnidade;

    @Column(precision = 10, scale = 2)
    private BigDecimal estoqueAtual;

    @Column(precision = 10, scale = 2)
    private BigDecimal estoqueInicial;

    // Getters e Setters

    public BigDecimal getEstoqueInicial() {
        return estoqueInicial;
    }

    public void setEstoqueInicial(BigDecimal estoqueInicial) {
        this.estoqueInicial = estoqueInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getCustoPorUnidade() {
        return custoPorUnidade;
    }

    public void setCustoPorUnidade(BigDecimal custoPorUnidade) {
        this.custoPorUnidade = custoPorUnidade;
    }

    public BigDecimal getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(BigDecimal estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }
}