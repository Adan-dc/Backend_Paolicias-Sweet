package com.microservicio.microservicio_comprobante_pago.controller;

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

import com.microservicio.microservicio_comprobante_pago.model.ComprobantePago;
import com.microservicio.microservicio_comprobante_pago.service.ComprobantePagoService;



@RestController
@RequestMapping("/api/comprobantes")
public class ComprobantePagoController {

    @Autowired
    private ComprobantePagoService comprobantePagoService;


    @GetMapping
    public List<ComprobantePago> listar() {
        return comprobantePagoService.listarTodos();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ComprobantePago> obtener(@PathVariable Long id) {
        ComprobantePago comprobante = comprobantePagoService.obtenerComprobante(id);
        if (comprobante == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(comprobante);
    }

    @GetMapping("/pago/{pagoId}")
    public List<ComprobantePago> porPago(@PathVariable Long pagoId) {
        return comprobantePagoService.buscarPorPago(pagoId);
    }

    @GetMapping("/estado/{estadoComprobante}")
    public List<ComprobantePago> porEstado(
            @PathVariable String estadoComprobante) {
        return comprobantePagoService
                .buscarPorEstado(estadoComprobante);
    }

    @PostMapping
    public ResponseEntity<ComprobantePago> crear(
            @RequestBody ComprobantePago comprobante) {
        return ResponseEntity.ok(
                comprobantePagoService.guardar(comprobante)
        );
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<ComprobantePago> aprobar(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                comprobantePagoService.aprobarComprobante(id)
        );
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ComprobantePago> rechazar(
            @PathVariable Long id,
            @RequestBody String observacion) {
        return ResponseEntity.ok(
                comprobantePagoService.rechazarComprobante(
                        id, observacion)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comprobantePagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumen")
    public List<Object[]> resumen() {
        return comprobantePagoService.resumenPorEstado();
    }
}
