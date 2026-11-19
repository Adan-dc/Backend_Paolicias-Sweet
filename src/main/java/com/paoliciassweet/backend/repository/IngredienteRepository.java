package com.paoliciassweet.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paoliciassweet.backend.model.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long>{
    
}
