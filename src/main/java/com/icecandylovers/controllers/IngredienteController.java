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
    public ResponseEntity<Map<String, Object>> salvarIngrediente(@RequestBody Ingrediente ingrediente) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (ingrediente.getNome() == null || ingrediente.getNome().trim().isEmpty()) {
                response.put("error", "Nome do ingrediente é obrigatório.");
                return ResponseEntity.badRequest().body(response);
            }
            if (ingrediente.getCustoPorUnidade() == null || ingrediente.getCustoPorUnidade().doubleValue() <= 0) {
                response.put("error", "Custo por unidade deve ser maior que zero.");
                return ResponseEntity.badRequest().body(response);
            }
            if (ingrediente.getUnidadeMedida() == null || ingrediente.getUnidadeMedida().trim().isEmpty()) {
                response.put("error", "Unidade de medida é obrigatória.");
                return ResponseEntity.badRequest().body(response);
            }
            if (ingrediente.getEstoqueInicial() == null || ingrediente.getEstoqueInicial().doubleValue() < 0) {
                response.put("error", "Estoque inicial não pode ser negativo.");
                return ResponseEntity.badRequest().body(response);
            }

            ingrediente.setEstoqueAtual(ingrediente.getEstoqueInicial());
            Ingrediente savedIngrediente = ingredienteService.salvar(ingrediente);

            response.put("id", savedIngrediente.getId());
            response.put("nome", savedIngrediente.getNome());
            response.put("custoPorUnidade", savedIngrediente.getCustoPorUnidade());
            response.put("unidadeMedida", savedIngrediente.getUnidadeMedida());
            response.put("estoqueAtual", savedIngrediente.getEstoqueAtual());
            response.put("closeModal", true);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Erro ao salvar ingrediente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
