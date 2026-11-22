package com.microservicios.microservicio_reservas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.microservicio_reservas.model.Reserva;
import com.microservicios.microservicio_reservas.service.ReservaService;
import com.microservicios.microservicio_reservas.model.ReservaProducto;
import com.microservicios.microservicio_reservas.model.CrearReservaRequest;
import com.microservicios.microservicio_reservas.model.ReservaPack;

@RestController 
@RequestMapping("/api/reservas")

public class ReservaController {

    @Autowired 
    private ReservaService reservaService;

    @GetMapping 
    public List<Reserva> listar(){
        return reservaService.listarTodos();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Reserva> obtener(@PathVariable Long id){
        Reserva reserva = reservaService.obtenerReserva(id);
        if(reserva==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/usuario/{usuarioEmail}")
    public List<Reserva> porUsuario(@PathVariable String usuarioEmail){
        return reservaService.buscarPorUsuario(usuarioEmail);
    }

    @GetMapping ("/estado/{estadoReserva}")
    public List<Reserva> porEstado(@PathVariable String estadoReserva){
        return reservaService.buscarPorEstado(estadoReserva);
    }

    @GetMapping ("/fecha")
    public List<Reserva> porFecha (@RequestParam String fecha){
        return reservaService.buscarPorFecha(fecha);
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(
            @RequestBody CrearReservaRequest datos) {

        return ResponseEntity.ok(reservaService.guardar(datos));
    }

    @PutMapping ("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmar(@PathVariable Long id){
        return ResponseEntity.ok(reservaService.confirmarReserva(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }


    @PutMapping("/{id}/aprobar-abono")
    public ResponseEntity<Reserva> aprobarAbono(@PathVariable Long id) {
            return ResponseEntity.ok(reservaService.aprobarAbono(id)
        );
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/resumen")
    public List<Object[]> resumen() {
        return reservaService.resumenPorEstado();
    }

    @GetMapping("/{id}/productos")
    public List<ReservaProducto> obtenerProductos(@PathVariable Long id) {

        return reservaService.obtenerProductosReserva(id);
    }


    @GetMapping("/{id}/packs")
    public List<ReservaPack> obtenerPacks(@PathVariable Long id) {

        return reservaService.obtenerPacksReserva(id);
    }

}