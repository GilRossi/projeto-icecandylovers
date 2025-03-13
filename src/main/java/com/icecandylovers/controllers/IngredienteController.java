package com.icecandylovers.controllers;

import com.icecandylovers.entities.Ingrediente;
import com.icecandylovers.services.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarIngrediente(@RequestBody Ingrediente ingrediente) {
        try {
            if (ingrediente.getNome() == null || ingrediente.getNome().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome do ingrediente é obrigatório.");
            }
            if (ingrediente.getCustoPorUnidade() == null || ingrediente.getCustoPorUnidade().doubleValue() <= 0) {
                return ResponseEntity.badRequest().body("Custo por unidade deve ser maior que zero.");
            }
            if (ingrediente.getUnidadeMedida() == null || ingrediente.getUnidadeMedida().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Unidade de medida é obrigatória.");
            }
            if (ingrediente.getEstoqueInicial() == null || ingrediente.getEstoqueInicial().doubleValue() < 0) {
                return ResponseEntity.badRequest().body("Estoque inicial não pode ser negativo.");
            }

            ingrediente.setEstoqueAtual(ingrediente.getEstoqueInicial());
            Ingrediente savedIngrediente = ingredienteService.salvar(ingrediente);

            Map<String, Object> response = new HashMap<>();
            response.put("id", savedIngrediente.getId());
            response.put("nome", savedIngrediente.getNome());
            response.put("custoPorUnidade", savedIngrediente.getCustoPorUnidade());
            response.put("unidadeMedida", savedIngrediente.getUnidadeMedida());
            response.put("estoqueAtual", savedIngrediente.getEstoqueAtual());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar ingrediente: " + e.getMessage());
        }
    }
}
