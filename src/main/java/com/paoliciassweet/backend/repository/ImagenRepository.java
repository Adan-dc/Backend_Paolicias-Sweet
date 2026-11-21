package com.paoliciassweet.backend.repository;

import com.paoliciassweet.backend.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
}