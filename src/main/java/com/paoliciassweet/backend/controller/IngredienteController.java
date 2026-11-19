package com.paoliciassweet.backend.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paoliciassweet.backend.model.Ingrediente;
import com.paoliciassweet.backend.service.IngredienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ingrediente")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @GetMapping
    public List<Ingrediente> listar() {
        return ingredienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> obternerPorId(@PathVariable Long id){
        return ResponseEntity.ok(ingredienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Ingrediente> crear(@Valid @RequestBody Ingrediente ingrediente){
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredienteService.guardar(ingrediente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingrediente> actualizar(@PathVariable Long id, @Valid @RequestBody Ingrediente ingredienteNuevo){
        return ResponseEntity.ok(ingredienteService.actualizar(id, ingredienteNuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        ingredienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
