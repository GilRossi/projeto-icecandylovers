package com.icecandylovers.services;

import com.icecandylovers.entities.Ingrediente;
import com.icecandylovers.repositories.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    @Autowired
    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<Ingrediente> listarTodos() {
        return ingredienteRepository.findAll();
    }

    public Optional<Ingrediente> buscarPorId(Long id) {
        return ingredienteRepository.findById(id);
    }

    public Ingrediente salvar(Ingrediente ingrediente) {
        if(ingrediente.getEstoqueAtual() == null) {
            ingrediente.setEstoqueAtual(ingrediente.getEstoqueInicial());
        }
        return ingredienteRepository.save(ingrediente);
    }
}
