package com.paoliciassweet.backend.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paoliciassweet.backend.model.Ingrediente;
import com.paoliciassweet.backend.repository.IngredienteRepository;

@Service
public class IngredienteService {
    @Autowired
    private IngredienteRepository ingredienteRepository;


    public List<Ingrediente> listarTodos() {
        return ingredienteRepository.findAll();
    }

    public Ingrediente buscarPorId(Long id){
        return ingredienteRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No existe el ingrediente con ese id"));
    }

    public Ingrediente guardar(Ingrediente ingrediente){
        ingrediente.setId(null);
        return ingredienteRepository.save(ingrediente);
    }

    public Ingrediente actualizar(Long id, Ingrediente ingredienteNuevo){
        Ingrediente ingredienteGuardado = buscarPorId(id);

        ingredienteGuardado.setNombre(ingredienteNuevo.getNombre());
        ingredienteGuardado.setPrecioUnidad(ingredienteNuevo.getPrecioUnidad());
        ingredienteGuardado.setUnidadMedida(ingredienteNuevo.getUnidadMedida());

        return ingredienteRepository.save(ingredienteGuardado);
    }

    public void eliminar(Long id) {
        Ingrediente ingrediente = buscarPorId(id);
        ingredienteRepository.delete(ingrediente);
    }

}
