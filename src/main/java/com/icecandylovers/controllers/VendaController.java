package com.icecandylovers.controllers;

import com.icecandylovers.dtos.VendaDTO;
import com.icecandylovers.entities.Venda;
import com.icecandylovers.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping("/recentes")
    public ResponseEntity<List<Venda>> obterVendasRecentes() {
        List<Venda> vendasRecentes = vendaService.obterVendasRecentes();
        return ResponseEntity.ok(vendasRecentes);
    }

    @GetMapping("/hoje")
    public ResponseEntity<BigDecimal> calcularVendasHoje() {
        BigDecimal totalVendasHoje = vendaService.calcularVendasHoje();
        return ResponseEntity.ok(totalVendasHoje);
    }

    @GetMapping("/recentes/top5")
    public ResponseEntity<List<Venda>> listarVendasRecentes() {
        List<Venda> vendasRecentes = vendaService.listarVendasRecentes();
        return ResponseEntity.ok(vendasRecentes);
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarVenda(@RequestBody VendaDTO vendaDTO) {
        vendaService.registrarVenda(vendaDTO);
        return ResponseEntity.ok("Venda registrada com sucesso!");
    }
}