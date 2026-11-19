package com.paoliciassweet.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paoliciassweet.backend.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

import com.paoliciassweet.backend.model.Producto;


@RestController
@RequestMapping("/api/v1/producto")
public class ProductoController {

    @Autowired 
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listaProductos(@RequestParam(required = false) Long categoriaId){
        if (categoriaId != null) {
            return productoService.listarPorCategoria(categoriaId);
        }
        return productoService.listarTodos();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }


    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto){
        return ResponseEntity.ok(productoService.agregarProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,  @RequestBody Producto productoNuevo){
        return ResponseEntity.ok(productoService.actualizar(id, productoNuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ingrediente/{ingredienteId}")
    public ResponseEntity<Producto> agregarIngrediente(@PathVariable Long id,
                                                       @PathVariable Long ingredienteId,
                                                       @RequestParam Double cantidad) {
        return ResponseEntity.ok(productoService.agregarIngrediente(id, ingredienteId, cantidad));
    }
    @DeleteMapping("/{id}/ingrediente/{ingredienteId}")
    public ResponseEntity<Producto> quitarIngrediente(@PathVariable Long id, @PathVariable Long ingredienteId) {
        return ResponseEntity.ok(productoService.quitarIngrediente(id, ingredienteId));
    }
}