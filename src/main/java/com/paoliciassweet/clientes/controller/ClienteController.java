package com.paoliciassweet.clientes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paoliciassweet.clientes.model.Cliente;
import com.paoliciassweet.clientes.service.ClienteService;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listar(@RequestParam(required = false) Long empresaId) {
        return clienteService.listarTodos(empresaId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> registrar(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.guardar(cliente));
    }

    @PostMapping("/login")
    public ResponseEntity<Cliente> login(@RequestBody Cliente credenciales) {
        return ResponseEntity.ok(clienteService.login(credenciales.getEmail(), credenciales.getPassword()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente clienteNuevo) {
        return ResponseEntity.ok(clienteService.actualizar(id, clienteNuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
