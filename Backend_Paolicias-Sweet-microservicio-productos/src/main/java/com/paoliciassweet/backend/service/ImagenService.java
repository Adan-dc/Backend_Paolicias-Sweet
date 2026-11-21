package com.paoliciassweet.backend.service;

import com.paoliciassweet.backend.model.Imagen;
import com.paoliciassweet.backend.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    public Imagen guardar(MultipartFile archivo) throws IOException {
        if (archivo.isEmpty()) {
            throw new RuntimeException("El archivo esta vacio");
        }

        String tipo = archivo.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) {
            throw new RuntimeException("El archivo debe ser una imagen");
        }

        return imagenRepository.save(new Imagen(null, tipo, archivo.getBytes()));
    }

    public Imagen buscarPorId(Long id) {
        return imagenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la imagen con id " + id));
    }
}