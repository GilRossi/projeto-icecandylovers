package com.icecandylovers.services;

import com.icecandylovers.entities.Taxa;
import com.icecandylovers.repositories.TaxaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxaService {

    private Taxa taxas = new Taxa();

    @Autowired
    private TaxaRepository taxaRepository;

    // Métodos para obter taxas individuais
    public Double getTaxaQuadrichama() {
        return taxas.getQuadrichama();
    }

    public Double getTaxaRapido() {
        return taxas.getRapido();
    }

    public Double getTaxaSemiRapido() {
        return taxas.getSemirapido();
    }

    //Método para salvar todas as taxas
    public void salvarTaxas(Taxa novasTaxas) {

        this.taxas.setQuadrichama(novasTaxas.getQuadrichama());
        this.taxas.setRapido(novasTaxas.getRapido());
        this.taxas.setSemirapido(novasTaxas.getSemirapido());
        this.taxas.setTaxaGas(novasTaxas.getTaxaGas());
        this.taxas.setAgua(novasTaxas.getAgua());
        this.taxas.setEnergia(novasTaxas.getEnergia());
        this.taxas.setKcal(novasTaxas.getKcal());
        this.taxas.setMj(novasTaxas.getMj());

        this.taxas.setPrecoGalao(novasTaxas.getPrecoGalao());
        this.taxas.setCapacidadeGalao(novasTaxas.getCapacidadeGalao());
        this.taxas.setAguaTorneira(novasTaxas.getAguaTorneira());

        taxaRepository.save(novasTaxas);
    }

    // Obtém a última taxa cadastrada
    public Taxa obterUltimasTaxas() {
        return taxaRepository.findFirstByOrderByIdDesc().orElse(new Taxa());
    }

    public Taxa obterTodasTaxas() {
        return taxas;
    }

    public Double getTaxaSemirapido() {
        return taxas.getSemirapido();
    }
}