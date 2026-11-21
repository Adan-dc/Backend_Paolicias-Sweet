package com.paoliciassweet.clientes.service;

import com.paoliciassweet.clientes.model.Empresa;
import com.paoliciassweet.clientes.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> listarTodas(String razonSocial) {
        if (razonSocial != null && !razonSocial.isBlank()) {
            return empresaRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
        }
        return empresaRepository.findAll();
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la empresa con id " + id));
    }

    public Empresa guardar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public Empresa login(String rut, String password) {
        Empresa empresa = empresaRepository.findByRut(rut)
                .orElseThrow(() -> new RuntimeException("Rut o contraseña incorrectos"));
        if (password == null || !password.equals(empresa.getPassword())) {
            throw new RuntimeException("Rut o contraseña incorrectos");
        }
        return empresa;
    }

    public Empresa actualizar(Long id, Empresa empresaNueva) {
        Empresa empresa = buscarPorId(id);
        empresa.setRut(empresaNueva.getRut());
        empresa.setRazonSocial(empresaNueva.getRazonSocial());
        empresa.setTelefono(empresaNueva.getTelefono());
        if (empresaNueva.getPassword() != null && !empresaNueva.getPassword().isBlank()) {
            empresa.setPassword(empresaNueva.getPassword());
        }
        return empresaRepository.save(empresa);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        empresaRepository.deleteById(id);
    }
}
