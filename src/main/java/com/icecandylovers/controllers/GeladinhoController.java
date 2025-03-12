package com.icecandylovers.controllers;

import com.icecandylovers.entities.ProdutoIngrediente;
import com.icecandylovers.services.IngredienteService;
import com.icecandylovers.services.TaxaService;
import org.springframework.ui.Model;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String salvarGeladinho(@ModelAttribute("produto") Produto produto, Model model) {
        try {
            // Calcula o preço de custo
            double precoCusto = calcularCustoTotal(produto);
            produto.setPrecoCusto(precoCusto);

            // Salva o produto no banco de dados
            produtoService.salvarProduto(produto);

            // Adiciona o objeto atualizado ao modelo
            model.addAttribute("produto", produto);
            model.addAttribute("mensagem", "Produto salvo com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar o produto: " + e.getMessage());
        }

        return "cadastro-geladinho";
    }

    private double calcularCustoTotal(Produto produto) {
        double custoTotal = 0;

        // Custo dos ingredientes
        for (ProdutoIngrediente ingrediente : produto.getIngredientes()) {
            if (ingrediente.getIngrediente() != null && ingrediente.getQuantidade() != null) {
                double custoUnitario = ingrediente.getIngrediente().getCustoPorUnidade().doubleValue();
                double quantidade = ingrediente.getQuantidade().doubleValue();
                if (custoUnitario > 0 && quantidade > 0) {
                    custoTotal += custoUnitario * quantidade;
                }
            }
        }

        // Custo da água
        if (produto.getFonteAgua() != null && produto.getTaxaAgua() != null) {
            if (produto.getFonteAgua().equals("GALAO") && produto.getQuantidadeGaloes() != null) {
                custoTotal += produto.getQuantidadeGaloes() * produto.getTaxaAgua();
            } else if (produto.getFonteAgua().equals("TORNEIRA") && produto.getMetrosCubicosAgua() != null) {
                custoTotal += produto.getMetrosCubicosAgua() * produto.getTaxaAgua();
            }
        }

        // Custo do gás
        if (produto.getHorasGas() != null && produto.getTaxaGas() != null) {
            if (produto.getHorasGas() > 0 && produto.getTaxaGas() > 0) {
                custoTotal += produto.getHorasGas() * produto.getTaxaGas();
            }
        }

        //Custo de Energia
        if (produto.getKwh() != null && produto.getTaxaEnergia() != null) {
            custoTotal += produto.getKwh() * produto.getTaxaEnergia();
        }

        return custoTotal;
    }

    @GetMapping("/novo")
    public String showCadastroForm(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("allIngredientes", ingredienteService.listarTodos());

        // Taxas de gás fogão/cooktop
        model.addAttribute("taxaQuadrichama", taxaService.getTaxaQuadrichama());
        model.addAttribute("taxaRapido", taxaService.getTaxaRapido());
        model.addAttribute("taxaSemiRapido", taxaService.getTaxaSemiRapido());

        return "cadastro-geladinho";
    }
}