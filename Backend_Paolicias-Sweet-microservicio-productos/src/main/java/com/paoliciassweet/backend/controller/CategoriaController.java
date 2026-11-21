package com.paoliciassweet.backend.controller;

import com.paoliciassweet.backend.model.Categoria;
import com.paoliciassweet.backend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Categoria buscar(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    public Categoria guardar(@RequestBody Categoria categoria) {
        return categoriaService.guardar(categoria);
    }

    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @RequestBody Categoria categoriaNueva) {
        return categoriaService.actualizar(id, categoriaNueva);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }
}