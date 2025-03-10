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
        model.addAttribute("taxas", taxas);
        return "cadastro-taxas";
    }

    @PostMapping("/salvar")
    public String salvarTaxas(@ModelAttribute("taxas") Taxa taxas) {
        taxaService.salvarTaxas(taxas);
        return "redirect:/taxas/editar";
    }
}