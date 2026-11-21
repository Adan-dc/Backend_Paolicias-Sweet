package com.paoliciassweet.clientes.controller;

import com.paoliciassweet.clientes.model.Empresa;
import com.paoliciassweet.clientes.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<Empresa> listar(@RequestParam(required = false) String razonSocial) {
        return empresaService.listarTodas(razonSocial);
    }

    @GetMapping("/{id}")
    public Empresa buscar(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    @PostMapping
    public Empresa guardar(@RequestBody Empresa empresa) {
        return empresaService.guardar(empresa);
    }

    @PostMapping("/login")
    public Empresa login(@RequestBody Empresa credenciales) {
        return empresaService.login(credenciales.getRut(), credenciales.getPassword());
    }

    @PutMapping("/{id}")
    public Empresa actualizar(@PathVariable Long id, @RequestBody Empresa empresaNueva) {
        return empresaService.actualizar(id, empresaNueva);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        empresaService.eliminar(id);
    }
}