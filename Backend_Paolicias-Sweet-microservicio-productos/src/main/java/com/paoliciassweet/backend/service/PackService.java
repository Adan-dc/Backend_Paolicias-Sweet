package com.paoliciassweet.backend.service;


import com.paoliciassweet.backend.model.Pack;
import com.paoliciassweet.backend.model.PackProducto;
import com.paoliciassweet.backend.model.Producto;
import com.paoliciassweet.backend.repository.PackRepository;
import com.paoliciassweet.backend.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PackService {
    @Autowired
    private PackRepository packRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Pack> listarTodos(Integer personas, String tipo){
        return packRepository.findAll().stream()
                .filter(p -> personas == null || personas.equals(p.getPersonas()))
                .filter(p -> tipo == null || tipo.equalsIgnoreCase(p.getTipo()))
                .toList();
    }

    public Pack buscarPorId(Long id){
        return packRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el pack con id " + id));
    }

    public Pack guardar(Pack pack){
        pack.setId(null);
        pack.setProductos(new ArrayList<>());
        if (pack.getActivo() == null) {
            pack.setActivo(true);
        }
        return packRepository.save(pack);
    }

    public Pack actualizar(Long id, Pack packNuevo) {
        Pack packGuardado = buscarPorId(id);

        packGuardado.setNombre(packNuevo.getNombre());
        packGuardado.setImagenUrl(packNuevo.getImagenUrl());
        packGuardado.setTipo(packNuevo.getTipo());
        packGuardado.setPrecio(packNuevo.getPrecio());
        packGuardado.setPersonas(packNuevo.getPersonas());
        if (packNuevo.getActivo() != null) {
            packGuardado.setActivo(packNuevo.getActivo());
        }

        return packRepository.save(packGuardado);
    }

    public void eliminar(Long id) {
        Pack pack = buscarPorId(id);
        packRepository.delete(pack);
    }


    @Transactional
    public Pack agregarProducto(Long packId, Long productoId, Integer cantidad, String grupo) {
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        Pack pack = buscarPorId(packId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("No existe el producto con id " + productoId));

        for (PackProducto item : pack.getProductos()) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(cantidad);
                item.setGrupo(grupo);
                return packRepository.save(pack);
            }
        }

        pack.getProductos().add(new PackProducto(null, pack, producto, cantidad, grupo));
        return packRepository.save(pack);
    }

    @Transactional
    public Pack quitarProducto(Long packId, Long productoId) {
        Pack pack = buscarPorId(packId);

        boolean quitado = pack.getProductos()
                .removeIf(item -> item.getProducto().getId().equals(productoId));
        if (!quitado) {
            throw new RuntimeException("El pack " + packId + " no tiene el producto " + productoId);
        }

        return packRepository.save(pack);
    }
}
