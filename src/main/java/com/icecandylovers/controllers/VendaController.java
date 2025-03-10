package com.icecandylovers.controllers;

import com.icecandylovers.dtos.VendaDTO;
import com.icecandylovers.services.ProdutoService;
import com.icecandylovers.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/nova")
    public String exibirFormVenda(Model model) {
        model.addAttribute("venda", new VendaDTO());
        model.addAttribute("produtos", produtoService.listarTodosProdutos());
        return "venda-form";
    }

    @PostMapping("/nova")
    public String processarVenda(@ModelAttribute VendaDTO vendaDTO) {
        vendaService.registrarVenda(vendaDTO);
        return "redirect:/dashboard";
    }
}