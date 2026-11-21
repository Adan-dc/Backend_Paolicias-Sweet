package com.paoliciassweet.backend.controller;

import com.paoliciassweet.backend.model.Imagen;
import com.paoliciassweet.backend.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/imagen")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping
    public ResponseEntity<Map<String, String>> subir(@RequestParam("archivo") MultipartFile archivo) throws IOException {
        Imagen guardada = imagenService.guardar(archivo);
        return ResponseEntity.ok(Map.of("nombre", "imagen-" + guardada.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtener(@PathVariable Long id) {
        Imagen imagen = imagenService.buscarPorId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imagen.getTipo()))
                .body(imagen.getContenido());
    }
}