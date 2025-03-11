package com.icecandylovers.controllers;

import com.icecandylovers.services.IngredienteService;
import com.icecandylovers.services.TaxaService;
import org.springframework.ui.Model;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/geladinhos")
public class GeladinhoController {

    private final ProdutoService produtoService;
    private final IngredienteService ingredienteService;
    private final TaxaService taxaService;

    @Autowired
    public GeladinhoController(ProdutoService produtoService,
                               IngredienteService ingredienteService,
                               TaxaService taxaService) {
        this.produtoService = produtoService;
        this.ingredienteService = ingredienteService;
        this.taxaService = taxaService;
    }

   @GetMapping("/editar/{id}")
    public String editarGeladinho(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarProdutoPorId(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        model.addAttribute("produto", produto);
        model.addAttribute("allIngredientes", ingredienteService.listarTodos());
        return "cadastro-geladinho";
    }

    @PostMapping("/salvar")
    public String salvarGeladinho(@ModelAttribute Produto produto,
                                  @RequestParam Map<String, String> params) {
        produtoService.salvarProdutoComIngredientes(produto, params);
        return "redirect:/geladinhos";
    }
    @GetMapping("/novo")
    public String showCadastroForm(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("allIngredientes", ingredienteService.listarTodos());

        //Taxas de gás fogão/cooktop
        model.addAttribute("taxaQuadrichama", taxaService.getTaxaQuadrichama());
        model.addAttribute("taxaRapido", taxaService.getTaxaRapido());
        model.addAttribute("taxaSemiRapido", taxaService.getTaxaSemiRapido());

        model.addAttribute("allIngredientes", ingredienteService.listarTodos());
        return "cadastro-geladinho";
    }
}