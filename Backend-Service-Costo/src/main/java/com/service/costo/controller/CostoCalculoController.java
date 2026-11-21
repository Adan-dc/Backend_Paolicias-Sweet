package com.service.costo.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.costo.dto.ProductoCostoDTO;
import com.service.costo.model.CostoCalculo;
import com.service.costo.service.CostoCalculoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequestMapping ("/api/v1/costo")
public class CostoCalculoController {

    @Autowired 
    private CostoCalculoService costoCalculoService;

    @GetMapping
    public List<CostoCalculo> listar(){
        return costoCalculoService.listarTodos();
    }

    @GetMapping("/detalle/{idReserva}")
    public List<ProductoCostoDTO> obtenerDetalleCosto(
            @PathVariable Long idReserva
    ) {
        return costoCalculoService
                .obtenerDetalleCosto(idReserva);
    }

    @PostMapping("/calculo")
    public CostoCalculo calcularCosto(@RequestBody Map<String, Long> datos){

        Long idReserva = datos.get("idReserva");

        return costoCalculoService.calcularCosto(idReserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        costoCalculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    
    
}
