package com.microservicio.microservicio_comprobante_pago.controller;

import java.util.List;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


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


    @PostMapping(value = "/subir", consumes = "multipart/form-data")
    public ResponseEntity<ComprobantePago> subir(
            @RequestParam("pagoId") Long pagoId,
            @RequestPart("archivo") MultipartFile archivo) {

        return ResponseEntity.ok(
                comprobantePagoService.subirArchivo(
                        pagoId, archivo)
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


    @GetMapping("/{id}/archivo")
    public ResponseEntity<byte[]> verArchivo(
            @PathVariable Long id) {
        ComprobantePago comprobante =
                comprobantePagoService.obtenerComprobante(id);
        if (comprobante == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] archivo =
                comprobantePagoService.descargarArchivo(id);

        MediaType tipo = switch (
                comprobante.getTipoArchivo()) {

            case "image/png" -> MediaType.IMAGE_PNG;
            case "image/jpeg" -> MediaType.IMAGE_JPEG;
            case "application/pdf" -> MediaType.APPLICATION_PDF;

            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
        ContentDisposition disposicion =
                ContentDisposition.inline()
                        .filename(
                                comprobante.getNombreArchivo(),
                                StandardCharsets.UTF_8)
                        .build();
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposicion.toString())
                .contentType(tipo)
                .body(archivo);
    }

}
