package com.icecandylovers.repositories;

import com.icecandylovers.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venda v " +
            "WHERE CAST(v.dataVenda AS date) = CURRENT_DATE")
    BigDecimal findTotalVendasHoje();

    List<Venda> findByDataVendaAfter(LocalDateTime dataInicio);

    List<Venda> findByDataVendaBetween(LocalDateTime start, LocalDateTime end);

    List<Venda> findTop10ByOrderByDataVendaDesc();

    List<Venda> findTop5ByOrderByDataVendaDesc();
}
