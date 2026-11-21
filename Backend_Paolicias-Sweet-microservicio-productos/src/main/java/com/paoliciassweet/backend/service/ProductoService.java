package com.paoliciassweet.backend.service;

import java.util.List;
import java.util.Optional;

import com.paoliciassweet.backend.model.Ingrediente;
import com.paoliciassweet.backend.model.ProductoIngrediente;
import com.paoliciassweet.backend.repository.IngredienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paoliciassweet.backend.model.Categoria;
import com.paoliciassweet.backend.model.Producto;
import com.paoliciassweet.backend.repository.ProductoRepository;

@Service 
public class ProductoService {

    @Autowired 
    private ProductoRepository productoRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;



    public Producto buscarPorId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el producto con id "+ id));
    }


    public List<Producto> listarTodos(){
        return productoRepository.findAll();
    }

    public Producto agregarProducto(Producto producto){
        producto.setId(null);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }

    public Producto actualizar(Long id, Producto productoNuevo) {

        Producto productoGuardado = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el producto con id " + id));

        productoGuardado.setNombre(productoNuevo.getNombre());
        productoGuardado.setPrecioBase(productoNuevo.getPrecioBase());
        productoGuardado.setImagenUrl(productoNuevo.getImagenUrl());
        productoGuardado.setCategoria(productoNuevo.getCategoria());
        productoGuardado.setDescripcion(productoNuevo.getDescripcion());

        return productoRepository.save(productoGuardado);
    }

    public List<Producto> listarPorCategoria(Long categoriaId){
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Transactional
    public Producto agregarIngrediente(Long productoId, Long ingredienteId, Double cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        Producto producto = buscarPorId(productoId);
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new RuntimeException("No existe el ingrediente con id " + ingredienteId));

        for (ProductoIngrediente item : producto.getIngredientes()) {
            if (item.getIngrediente().getId().equals(ingrediente.getId())) {
                item.setCantidad(cantidad);
                return productoRepository.save(producto);
            }
        }

        producto.getIngredientes().add(new ProductoIngrediente(null, producto, ingrediente, cantidad));
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto quitarIngrediente(Long productoId, Long ingredienteId) {
        Producto producto = buscarPorId(productoId);

        boolean quitado = producto.getIngredientes()
                .removeIf(item -> item.getIngrediente().getId().equals(ingredienteId));
        if (!quitado) {
            throw new RuntimeException("El producto " + productoId + " no tiene el ingrediente " + ingredienteId);
        }

        return productoRepository.save(producto);
    }

}
