package com.paoliciassweet.backend.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paoliciassweet.backend.model.Pack;
import com.paoliciassweet.backend.service.PackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pack")
public class PackController {
    @Autowired
    private PackService packService;
    @GetMapping
    public List<Pack> listar(@RequestParam(required = false) Integer personas,
                             @RequestParam(required = false) String tipo) {
        return packService.listarTodos(personas, tipo);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Pack> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(packService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pack> crear(@Valid @RequestBody Pack pack) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packService.guardar(pack));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pack> actualizar(@PathVariable Long id, @Valid @RequestBody Pack packNuevo) {
        return ResponseEntity.ok(packService.actualizar(id, packNuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        packService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/producto/{productoId}")
    public ResponseEntity<Pack> agregarProducto(@PathVariable Long id, @PathVariable Long productoId,
                                                @RequestParam Integer cantidad, @RequestParam(required = false) String grupo) {
        return ResponseEntity.ok(packService.agregarProducto(id, productoId, cantidad, grupo));
    }

    @DeleteMapping("/{id}/producto/{productoId}")
    public ResponseEntity<Pack> quitarProducto(@PathVariable Long id, @PathVariable Long productoId) {
        return ResponseEntity.ok(packService.quitarProducto(id, productoId));
    }
}
