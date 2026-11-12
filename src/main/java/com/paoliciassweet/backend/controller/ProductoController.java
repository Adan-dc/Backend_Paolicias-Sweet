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
@RequestMapping("/api/v1/stock")
public class ProductoController {

    @Autowired 
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listaProductos(){
        return productoService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto){
        return ResponseEntity.ok(productoService.agregarProducto(producto));
    }

    @PutMapping("/{id}/productoNuevo")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id){
        return ResponseEntity.ok(productoService.actualizar(id, productoNuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}