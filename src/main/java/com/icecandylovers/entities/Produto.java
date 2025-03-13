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

    @Column(nullable = false)
    private String sabor;

    @Column(nullable = false)
    private int estoqueAtual;

    @Column(nullable = false)
    private Integer estoqueInicial;

    @Column(name = "preco_custo", nullable = false)
    private Double precoCusto;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoIngrediente> ingredientes = new ArrayList<>();

    @Column(name = "preco_venda", nullable = true)
    private BigDecimal precoVenda;

    @Column(name = "horas_producao")
    private Double horasProducao;

    // Campos para cálculo de gás
    private Double kcalOperadora;
    private Double mj;
    private Double kwh;

    // Taxas de gás
    private Double quadrichama;
    private Double rapido;
    private Double semirapido;

    // kWh/Kg calculados
    private Double kwhKgQuadrichama;
    private Double kwhKgRapido;
    private Double kwhKgSemirapido;

    private Double horasAgua;
    private Double horasGas;
    private Double horasEnergia;

    private Double taxaAgua;
    private Double taxaGas;
    private Double taxaEnergia;

    @Column(name = "uso_quadrichama")
    private Boolean usoQuadrichama;

    @Column(name = "uso_rapido")
    private Boolean usoRapido;

    @Column(name = "uso_semirapido")
    private Boolean usoSemirapido;

    // Água
    @Column(name = "fonte_agua")
    private String fonteAgua;

    @Column(name = "quantidade_galoes")
    private Double quantidadeGaloes;

    @Column(name = "metros_cubicos_agua")
    private Double metrosCubicosAgua;

    // Custos fixos
    @Column(name = "custo_agua")
    private Double custoAgua;

    @Column(name = "custo_gas")
    private Double custoGas;

    @Column(name = "custo_energia")
    private Double custoEnergia;

    // Getters e Setters

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

    public int getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public Integer getEstoqueInicial() {
        return estoqueInicial;
    }

    public void setEstoqueInicial(Integer estoqueInicial) {
        this.estoqueInicial = estoqueInicial;
    }

    public Double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(Double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public List<ProdutoIngrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<ProdutoIngrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Double getHorasProducao() {
        return horasProducao;
    }

    public void setHorasProducao(Double horasProducao) {
        this.horasProducao = horasProducao;
    }

    public Double getKcalOperadora() {
        return kcalOperadora;
    }

    public void setKcalOperadora(Double kcalOperadora) {
        this.kcalOperadora = kcalOperadora;
    }

    public Double getMj() {
        return mj;
    }

    public void setMj(Double mj) {
        this.mj = mj;
    }

    public Double getKwh() {
        return kwh;
    }

    public void setKwh(Double kwh) {
        this.kwh = kwh;
    }

    public Double getQuadrichama() {
        return quadrichama;
    }

    public void setQuadrichama(Double quadrichama) {
        this.quadrichama = quadrichama;
    }

    public Double getRapido() {
        return rapido;
    }

    public void setRapido(Double rapido) {
        this.rapido = rapido;
    }

    public Double getSemirapido() {
        return semirapido;
    }

    public void setSemirapido(Double semirapido) {
        this.semirapido = semirapido;
    }

    public Double getKwhKgQuadrichama() {
        return kwhKgQuadrichama;
    }

    public void setKwhKgQuadrichama(Double kwhKgQuadrichama) {
        this.kwhKgQuadrichama = kwhKgQuadrichama;
    }

    public Double getKwhKgRapido() {
        return kwhKgRapido;
    }

    public void setKwhKgRapido(Double kwhKgRapido) {
        this.kwhKgRapido = kwhKgRapido;
    }

    public Double getKwhKgSemirapido() {
        return kwhKgSemirapido;
    }

    public void setKwhKgSemirapido(Double kwhKgSemirapido) {
        this.kwhKgSemirapido = kwhKgSemirapido;
    }

    public Double getHorasAgua() {
        return horasAgua;
    }

    public void setHorasAgua(Double horasAgua) {
        this.horasAgua = horasAgua;
    }

    public Double getHorasGas() {
        return horasGas;
    }

    public void setHorasGas(Double horasGas) {
        this.horasGas = horasGas;
    }

    public Double getHorasEnergia() {
        return horasEnergia;
    }

    public void setHorasEnergia(Double horasEnergia) {
        this.horasEnergia = horasEnergia;
    }

    public Double getTaxaAgua() {
        return taxaAgua;
    }

    public void setTaxaAgua(Double taxaAgua) {
        this.taxaAgua = taxaAgua;
    }

    public Double getTaxaGas() {
        return taxaGas;
    }

    public void setTaxaGas(Double taxaGas) {
        this.taxaGas = taxaGas;
    }

    public Double getTaxaEnergia() {
        return taxaEnergia;
    }

    public void setTaxaEnergia(Double taxaEnergia) {
        this.taxaEnergia = taxaEnergia;
    }

    public Boolean getUsoQuadrichama() {
        return usoQuadrichama;
    }

    public void setUsoQuadrichama(Boolean usoQuadrichama) {
        this.usoQuadrichama = usoQuadrichama;
    }

    public Boolean getUsoRapido() {
        return usoRapido;
    }

    public void setUsoRapido(Boolean usoRapido) {
        this.usoRapido = usoRapido;
    }

    public Boolean getUsoSemirapido() {
        return usoSemirapido;
    }

    public void setUsoSemirapido(Boolean usoSemirapido) {
        this.usoSemirapido = usoSemirapido;
    }

    public String getFonteAgua() {
        return fonteAgua;
    }

    public void setFonteAgua(String fonteAgua) {
        this.fonteAgua = fonteAgua;
    }

    public Double getQuantidadeGaloes() {
        return quantidadeGaloes;
    }

    public void setQuantidadeGaloes(Double quantidadeGaloes) {
        this.quantidadeGaloes = quantidadeGaloes;
    }

    public Double getMetrosCubicosAgua() {
        return metrosCubicosAgua;
    }

    public void setMetrosCubicosAgua(Double metrosCubicosAgua) {
        this.metrosCubicosAgua = metrosCubicosAgua;
    }

    public Double getCustoAgua() {
        return custoAgua;
    }

    public void setCustoAgua(Double custoAgua) {
        this.custoAgua = custoAgua;
    }

    public Double getCustoGas() {
        return custoGas;
    }

    public void setCustoGas(Double custoGas) {
        this.custoGas = custoGas;
    }

    public Double getCustoEnergia() {
        return custoEnergia;
    }

    public void setCustoEnergia(Double custoEnergia) {
        this.custoEnergia = custoEnergia;
    }
}