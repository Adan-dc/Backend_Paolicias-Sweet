package com.microservicios.microservicio_pago.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.microservicios.microservicio_pago.model.Pago;
import com.microservicios.microservicio_pago.service.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<Pago> listar() {
        return pagoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtener(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPago(id);
        if (pago == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/reserva/{reservaId}")
    public List<Pago> porReserva(@PathVariable Long reservaId) {
        return pagoService.buscarPorReserva(reservaId);
    }

    @GetMapping("/estado/{estadoPago}")
    public List<Pago> porEstado(@PathVariable String estadoPago) {
        return pagoService.buscarPorEstado(estadoPago);
    }

    @GetMapping("/tipo/{tipoPago}")
    public List<Pago> porTipo(@PathVariable String tipoPago) {
        return pagoService.buscarPorTipo(tipoPago);
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.guardar(pago));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Pago> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.aprobarPago(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Pago> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.rechazarPago(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumen")
    public List<Object[]> resumen() {
        return pagoService.resumenPorEstado();
    }
}
