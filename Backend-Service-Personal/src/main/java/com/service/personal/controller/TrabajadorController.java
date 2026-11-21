package com.service.personal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.personal.model.Trabajador;
import com.service.personal.service.TrabajadorService;

@RestController  
@RequestMapping ("/api/v1/trabajadores")
public class TrabajadorController {
    @Autowired 
    private TrabajadorService trabajadorService;

    @GetMapping 
    public List<Trabajador> listar(){
        return trabajadorService.listarTodos();
    }

    @PostMapping("/crear")
    public Trabajador crear(@RequestBody Trabajador trabajador){
        return trabajadorService.crearTrabajador(trabajador);
    } 

    @PutMapping("/{idTrabajador}/asignacion")
    public Trabajador asignacion(@PathVariable Long idTrabajador, @RequestBody Map<String, Long> datos){
        Long idReserva = datos.get("idReserva");
        return trabajadorService.asignacionTrabajador(idReserva, idTrabajador);
    } 

    @PutMapping("/{idTrabajador}/cancelar-asignacion")
    public Trabajador cancelarAsignacion(
            @PathVariable Long idTrabajador
    ) {
        return trabajadorService.cancelarAsignacion(idTrabajador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trabajador> actualizarTrabajador(@PathVariable Long id, @RequestBody Trabajador trabajadorNuevo){
        Trabajador trabajadorActualizado = trabajadorService.actualizarTrabajador(id, trabajadorNuevo);
        return ResponseEntity.ok(trabajadorActualizado);
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTrabajador(@PathVariable Long id){
        trabajadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    } 
}
