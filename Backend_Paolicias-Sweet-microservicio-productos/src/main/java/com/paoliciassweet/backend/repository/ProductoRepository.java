package com.paoliciassweet.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paoliciassweet.backend.model.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    List<Producto> findByCategoriaId(Long categoriaId);
}
