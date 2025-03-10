package com.icecandylovers.entities;

import jakarta.persistence.*;

@Entity
public class Taxa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kwh_kg_quadrichama", nullable = true)
    private Double quadrichama;

    @Column(name = "kwh_kg_rapido", nullable = true)
    private Double rapido;

    @Column(name = "kwh_kg_semirapido", nullable = true)
    private Double semirapido;

    // Adicione anotações para água e energia
    @Column(name = "agua", nullable = false)
    private Double agua;

    @Column(name = "energia", nullable = false)
    private Double energia;

    @Column(name = "taxaGas", nullable = false)
    private Double taxaGas;

    @Column(name = "kcal")
    private Double kcal;

    @Column(name = "mj")
    private Double mj;

    @Column(name = "kwh")
    private Double kwh;


    //Getters and Setters


    public Double getTaxaGas() {
        return taxaGas;
    }

    public void setTaxaGas(Double taxaGas) {
        this.taxaGas = taxaGas;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getAgua() {
        return agua;
    }

    public void setAgua(Double agua) {
        this.agua = agua;
    }

    public Double getEnergia() {
        return energia;
    }

    public void setEnergia(Double energia) {
        this.energia = energia;
    }
}
