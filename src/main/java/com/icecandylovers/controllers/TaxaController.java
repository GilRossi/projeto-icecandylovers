package com.icecandylovers.controllers;

import com.icecandylovers.entities.Taxa;
import com.icecandylovers.services.TaxaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/taxas")
public class TaxaController {

    @Autowired
    private TaxaService taxaService;

    // Carrega a página com os dados existentes
    @GetMapping("/editar")
    public String editarTaxas(Model model) {
        Taxa taxas = taxaService.obterUltimasTaxas();

        // Verifica se o kwh existe e não é zero
        if (taxas.getKwh() != null && taxas.getKwh() != 0) {
            taxas.setResultadoQuadrichama(taxas.getQuadrichama() / taxas.getKwh());
            taxas.setResultadoRapido(taxas.getRapido() / taxas.getKwh());
            taxas.setResultadoSemirapido(taxas.getSemirapido() / taxas.getKwh());
        } else {
            // Define resultados como null para evitar divisão por zero
            taxas.setResultadoQuadrichama(null);
            taxas.setResultadoRapido(null);
            taxas.setResultadoSemirapido(null);
        }

        model.addAttribute("taxas", taxas);
        return "cadastro-taxas";
    }

    public String salvarGas(@ModelAttribute("taxas") Taxa novasTaxas) {
        Taxa taxasAtuais = taxaService.obterUltimasTaxas();

        // Atualiza apenas campos relacionados ao Gás
        taxasAtuais.setTaxaGas(novasTaxas.getTaxaGas());
        taxasAtuais.setQuadrichama(novasTaxas.getQuadrichama());
        taxasAtuais.setRapido(novasTaxas.getRapido());
        taxasAtuais.setSemirapido(novasTaxas.getSemirapido());
        taxasAtuais.setKcal(novasTaxas.getKcal());
        taxasAtuais.setMj(novasTaxas.getMj());
        taxasAtuais.setKwh(novasTaxas.getKwh());

        taxaService.salvarTaxas(taxasAtuais);
        return "redirect:/taxas/editar";
    }

    @PostMapping("/salvar/agua")
    public String salvarAgua(@ModelAttribute("taxas") Taxa novasTaxas) {
        System.out.println("Recebendo taxa de água: " + novasTaxas.getAgua());

        Taxa taxasAtuais = taxaService.obterUltimasTaxas();

        if (taxasAtuais == null) {
            taxasAtuais = new Taxa();
        }

        if (novasTaxas.getAgua() == null) {
            System.out.println("Erro: O campo 'agua' está nulo!");
            return "redirect:/taxas/editar?erro=aguaNula";
        }

        taxasAtuais.setAgua(novasTaxas.getAgua());
        taxasAtuais.setPrecoGalao(novasTaxas.getPrecoGalao());
        taxasAtuais.setCapacidadeGalao(novasTaxas.getCapacidadeGalao());
        taxasAtuais.setAguaTorneira(novasTaxas.getAguaTorneira());

        taxaService.salvarTaxas(taxasAtuais);

        return "redirect:/taxas/editar";
    }


    @PostMapping("/salvar/energia")
    public String salvarEnergia(@ModelAttribute("taxas") Taxa novasTaxas) {
        Taxa taxasAtuais = taxaService.obterUltimasTaxas();
        taxasAtuais.setEnergia(novasTaxas.getEnergia()); // Atualiza apenas energia
        taxaService.salvarTaxas(taxasAtuais);
        return "redirect:/taxas/editar";
    }
}