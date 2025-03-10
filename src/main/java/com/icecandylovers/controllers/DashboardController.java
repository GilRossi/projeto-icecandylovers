package com.icecandylovers.controllers;

import com.icecandylovers.services.ProdutoService;
import com.icecandylovers.services.VendaService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ProdutoService produtoService;
    private final VendaService vendaService;

    @Autowired
    public DashboardController(ProdutoService produtoService, VendaService vendaService) {
        this.produtoService = produtoService;
        this.vendaService = vendaService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("produtos", produtoService.listarTodosProdutos());
        model.addAttribute("vendas", vendaService.obterVendasRecentes());
        model.addAttribute("totalEstoque", produtoService.calcularTotalEstoque());
        model.addAttribute("vendasHoje", vendaService.calcularVendasHoje());
        model.addAttribute("vendasRecentes", vendaService.listarVendasRecentes());
        return "dashboard";
    }
}