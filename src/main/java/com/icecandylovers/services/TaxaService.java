package com.icecandylovers.services;

import com.icecandylovers.entities.Taxa;
import com.icecandylovers.repositories.TaxaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxaService {

    @Autowired
    private TaxaRepository taxaRepository;

    // Métodos para obter taxas individuais
    public Double getTaxaQuadrichama() {
        Taxa taxas = obterUltimasTaxas();
        return taxas.getQuadrichama();
    }

    public Double getTaxaRapido() {
        Taxa taxas = obterUltimasTaxas();
        return taxas.getRapido();
    }

    public Double getTaxaSemiRapido() {
        Taxa taxas = obterUltimasTaxas();
        return taxas.getSemirapido();
    }

    // Método para salvar todas as taxas
    public void salvarTaxas(Taxa novasTaxas) {
        Taxa taxasAtuais = obterUltimasTaxas();

        // Atualiza os campos
        taxasAtuais.setQuadrichama(novasTaxas.getQuadrichama());
        taxasAtuais.setRapido(novasTaxas.getRapido());
        taxasAtuais.setSemirapido(novasTaxas.getSemirapido());
        taxasAtuais.setTaxaGas(novasTaxas.getTaxaGas());
        taxasAtuais.setAgua(novasTaxas.getAgua());
        taxasAtuais.setEnergia(novasTaxas.getEnergia());
        taxasAtuais.setKcal(novasTaxas.getKcal());
        taxasAtuais.setMj(novasTaxas.getMj());
        taxasAtuais.setPrecoGalao(novasTaxas.getPrecoGalao());
        taxasAtuais.setCapacidadeGalao(novasTaxas.getCapacidadeGalao());
        taxasAtuais.setAguaTorneira(novasTaxas.getAguaTorneira());

        taxaRepository.save(taxasAtuais);
    }

    // Obtém a última taxa cadastrada
    public Taxa obterUltimasTaxas() {
        return taxaRepository.findFirstByOrderByIdDesc().orElse(new Taxa());
    }
}