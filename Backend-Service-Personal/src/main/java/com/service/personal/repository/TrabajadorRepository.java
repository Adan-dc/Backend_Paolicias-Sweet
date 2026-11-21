package com.service.personal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.personal.model.Trabajador;

public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {
    
}
