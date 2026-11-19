package com.paoliciassweet.backend.service;

import com.paoliciassweet.backend.model.Categoria;
import com.paoliciassweet.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la categoría con id " + id));
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria categoriaNueva) {
        Categoria categoria = buscarPorId(id);
        categoria.setNombre(categoriaNueva.getNombre());
        categoria.setSeccion(categoriaNueva.getSeccion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        categoriaRepository.deleteById(id);
    }
}