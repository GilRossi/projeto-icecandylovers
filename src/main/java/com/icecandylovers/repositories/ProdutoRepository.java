package com.icecandylovers.repositories;

import com.icecandylovers.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT COALESCE(SUM(e.estoqueAtual), 0) FROM Produto e")
    int sumEstoqueAtual();
}
