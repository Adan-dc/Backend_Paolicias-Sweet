package com.paoliciassweet.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paoliciassweet.backend.model.Pack;
public interface PackRepository extends JpaRepository <Pack, Long>{
    List<Pack> findByPersonas(Integer personas);
}
