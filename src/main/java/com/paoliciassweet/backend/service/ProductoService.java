package com.paoliciassweet.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paoliciassweet.backend.model.Categoria;
import com.paoliciassweet.backend.model.Producto;
import com.paoliciassweet.backend.repository.ProductoRepository;

@Service 
public class ProductoService {

    @Autowired 
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos(){
        return productoRepository.findAll();
    }

    public Producto agregarProducto(Producto producto){

        try{
            if(producto.getId()!= null){

        String nombre = (producto.getNombre());

        producto.setNombre(nombre);

        Integer precioBase = ((Integer) producto.getPrecioBase());

        producto.setPrecioBase(precioBase);;

        String imagenUrl = (producto.getImagenUrl());

        producto.setImagenUrl(imagenUrl);

        Categoria categoria = (producto.getCategoria());

        producto.setCategoria(categoria);
    }
        } catch (Exception e) {
            e.printStackTrace();
        throw new RuntimeException("Microservicio de Inventario offline o producto no encontrado");
        }

        return productoRepository.save(producto);
    }
    

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto actualizar(Long id, Producto productoNuevo) {

        Optional<Producto> producto = productoRepository.findById(id);

        if (producto.isPresent()) {

            Producto productoGuardado = producto.get();

            productoGuardado.setNombre(productoNuevo.getNombre());
            productoGuardado.setPrecioBase(productoNuevo.getPrecioBase());

            productoGuardado.setImagenUrl(productoNuevo.getImagenUrl());

            Categoria categoria = (producto.getCategoria());
            productoGuardado.setCategoria(categoria);

            return productoRepository.save(productoGuardado);
        }
        return null; 
    }
    
}
