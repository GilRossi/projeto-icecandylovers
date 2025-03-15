package com.icecandylovers.controllers;

import com.icecandylovers.dtos.ProdutoDTO;
import com.icecandylovers.dtos.ProdutoIngredienteDTO;
import com.icecandylovers.entities.Produto;
import com.icecandylovers.services.GeladinhoService;
import com.icecandylovers.services.IngredienteService;
import com.icecandylovers.services.TaxaService;
import com.icecandylovers.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/geladinhos")
public class GeladinhoController {

    private final ProdutoService produtoService;
    private final IngredienteService ingredienteService;
    private final TaxaService taxaService;
    private final GeladinhoService geladinhoService;

    @Autowired
    public GeladinhoController(ProdutoService produtoService,
                               IngredienteService ingredienteService,
                               TaxaService taxaService,
                               GeladinhoService geladinhoService) {
        this.produtoService = produtoService;
        this.ingredienteService = ingredienteService;
        this.taxaService = taxaService;
        this.geladinhoService = geladinhoService;
    }

    @GetMapping("/editar/{id}")
    public String editarGeladinho(@PathVariable Long id, Model model) {
        try {
            // Verifica se o ID é válido
            if (id == null || id <= 0) {
                model.addAttribute("erro", "ID do produto inválido");
                return "redirect:/dashboard"; // Redireciona para a dashboard em caso de erro
            }

            // Busca o produto pelo ID
            Optional<Produto> produtoOptional = produtoService.buscarProdutoPorId(id);
            if (produtoOptional.isEmpty()) {
                model.addAttribute("erro", "Produto não encontrado");
                return "redirect:/dashboard"; // Redireciona para a dashboard em caso de erro
            }

            // Converte a entidade Produto para ProdutoDTO
            Produto produto = produtoOptional.get();
            ProdutoDTO produtoDTO = convertToProdutoDTO(produto);

            // Adiciona o produto e os ingredientes ao modelo
            model.addAttribute("produto", produtoDTO);
            model.addAttribute("allIngredientes", ingredienteService.listarTodos());

            // Adiciona as taxas ao modelo (se necessário)
            model.addAttribute("taxaQuadrichama", taxaService.getTaxaQuadrichama());
            model.addAttribute("taxaRapido", taxaService.getTaxaRapido());
            model.addAttribute("taxaSemiRapido", taxaService.getTaxaSemiRapido());

            return "cadastro-geladinho"; // Retorna a página de edição
        } catch (Exception e) {
            // Log do erro
            System.err.println("Erro ao editar geladinho: " + e.getMessage());
            model.addAttribute("erro", "Erro ao editar o produto");
            return "redirect:/dashboard"; // Redireciona para a dashboard em caso de erro
        }
    }

    @PostMapping("/editar")
    public String editarGeladinho(@ModelAttribute ProdutoDTO produtoDTO, Model model) {
        try {
            Produto produto = convertToProduto(produtoDTO);
            geladinhoService.atualizar(produto);
            model.addAttribute("mensagem", "Geladinho atualizado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar o geladinho: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/salvar")
    public ResponseEntity<Map<String, Object>> salvarGeladinho(@ModelAttribute ProdutoDTO produtoDTO) {
        // Validação de campos obrigatórios
        if (produtoDTO.sabor() == null || produtoDTO.sabor().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "O campo 'sabor' é obrigatório.");
            return ResponseEntity.badRequest().body(response);
        }

        // Filtrando ingredientes sem ID
        produtoDTO = new ProdutoDTO(
                produtoDTO.id(),
                produtoDTO.sabor(),
                produtoDTO.estoqueInicial(),
                produtoDTO.estoqueAtual(),
                produtoDTO.precoCusto(),
                produtoDTO.ingredientes().stream()
                        .filter(ing -> ing.ingredienteId() != null)
                        .toList(),
                produtoDTO.fonteAgua(),
                produtoDTO.quantidadeGaloes(),
                produtoDTO.metrosCubicosAgua(),
                produtoDTO.horasGas(),
                produtoDTO.kwh(),
                produtoDTO.taxaAgua(),
                produtoDTO.taxaGas(),
                produtoDTO.taxaEnergia(),
                produtoDTO.usoQuadrichama(),
                produtoDTO.usoRapido(),
                produtoDTO.usoSemirapido()
        );

        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("ProdutoDTO recebido: " + produtoDTO); // Log para depuração
            produtoService.salvarProduto(produtoDTO);
            response.put("success", true);
            response.put("message", "Geladinho salvo com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar o geladinho: " + e.getMessage()); // Log de erro
            response.put("success", false);
            response.put("error", "Erro ao salvar o geladinho: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/novo")
    public String showCadastroForm(Model model) {
        ProdutoDTO produtoDTO = new ProdutoDTO(
                null, // id
                "", // sabor
                0, // estoqueInicial
                0, // estoqueAtual
                0.0, // precoCusto
                //0.0, //precoCustoUnitario
                new ArrayList<>(), // ingredientes
                "", // fonteAgua
                0.0, // quantidadeGaloes
                0.0, // metrosCubicosAgua
                0.0, // horasGas
                0.0, // kwh
                0.0, // taxaAgua
                0.0, // taxaGas
                0.0, // taxaEnergia
                false, // usoQuadrichama
                false, // usoRapido
                false // usoSemirapido
        );

        System.out.println("ProdutoDTO inicializado: " + produtoDTO); // 🚀 Log para depuração

        model.addAttribute("produto", produtoDTO);
        model.addAttribute("allIngredientes", ingredienteService.listarTodos());
        return "cadastro-geladinho";
    }



    // Método para converter Produto para ProdutoDTO
    private ProdutoDTO convertToProdutoDTO(Produto produto) {
        List<ProdutoIngredienteDTO> ingredientesDTO = produto.getIngredientes().stream()
                .map(pi -> new ProdutoIngredienteDTO(pi.getIngrediente().getId(), pi.getQuantidade().doubleValue()))
                .toList();

        return new ProdutoDTO(
                produto.getId(),
                produto.getSabor(),
                produto.getEstoqueInicial(),
                produto.getEstoqueAtual(),
                produto.getPrecoCusto(),
                //produto.getPrecoCustoUnitario(),
                ingredientesDTO,
                produto.getFonteAgua(),
                produto.getQuantidadeGaloes(),
                produto.getMetrosCubicosAgua(),
                produto.getHorasGas(),
                produto.getKwh(),
                produto.getTaxaAgua(),
                produto.getTaxaGas(),
                produto.getTaxaEnergia(),
                produto.getUsoQuadrichama(),
                produto.getUsoRapido(),
                produto.getUsoSemirapido()
        );
    }

    @PostMapping("/deletar")
    public String deletarGeladinho(@RequestParam Long id) {
        geladinhoService.deletar(id);
        return "redirect:/dashboard";
    }
    private Produto convertToProduto(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setId(produtoDTO.id());
        produto.setSabor(produtoDTO.sabor());
        produto.setEstoqueInicial(produtoDTO.estoqueInicial());
        produto.setEstoqueAtual(produtoDTO.estoqueAtual());
        //produto.setPrecoCusto(produtoDTO.precoCusto());
        produto.setFonteAgua(produtoDTO.fonteAgua());
        produto.setQuantidadeGaloes(produtoDTO.quantidadeGaloes());
        produto.setMetrosCubicosAgua(produtoDTO.metrosCubicosAgua());
        produto.setHorasGas(produtoDTO.horasGas());
        produto.setKwh(produtoDTO.kwh());
        produto.setTaxaAgua(produtoDTO.taxaAgua());
        produto.setTaxaGas(produtoDTO.taxaGas());
        produto.setTaxaEnergia(produtoDTO.taxaEnergia());
        produto.setUsoQuadrichama(produtoDTO.usoQuadrichama());
        produto.setUsoRapido(produtoDTO.usoRapido());
        produto.setUsoSemirapido(produtoDTO.usoSemirapido());
        return produto;
    }
}