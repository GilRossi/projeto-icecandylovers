package com.icecandylovers.repositories;

import com.icecandylovers.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT SUM(p.estoqueAtual) FROM Produto p")
    int sumEstoqueAtual();
}
