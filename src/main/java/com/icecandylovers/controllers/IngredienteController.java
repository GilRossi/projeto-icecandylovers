package com.icecandylovers.controllers;

import com.icecandylovers.entities.Ingrediente;
import com.icecandylovers.services.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @PostMapping("/salvar")
    @ResponseBody
    public Ingrediente salvarIngrediente(@RequestBody Ingrediente ingrediente) {
        // Garante que o estoque atual seja inicializado
        if (ingrediente.getEstoqueAtual() == null) {
            ingrediente.setEstoqueAtual(ingrediente.getEstoqueInicial());
        }
        return ingredienteService.salvar(ingrediente);
    }
}