package com.icecandylovers.repositories;

import com.icecandylovers.entities.ProdutoIngrediente;
import com.icecandylovers.entities.ProdutoIngredienteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoIngredienteRepository extends JpaRepository<ProdutoIngrediente, ProdutoIngredienteId> {
}
