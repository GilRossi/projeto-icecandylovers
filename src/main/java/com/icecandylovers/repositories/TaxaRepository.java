package com.icecandylovers.repositories;

import com.icecandylovers.entities.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxaRepository extends JpaRepository<Taxa, Long> {
    Optional<Taxa> findFirstByOrderByIdDesc();
}
