package com.paoliciassweet.clientes.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paoliciassweet.clientes.model.Cliente;
import com.paoliciassweet.clientes.model.Empresa;
import com.paoliciassweet.clientes.repository.ClienteRepository;
import com.paoliciassweet.clientes.repository.EmpresaRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;


    public List<Cliente> listarTodos(Long empresaId) {
        if (empresaId != null) {
            return clienteRepository.findByEmpresaId(empresaId);
        }
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el cliente con id " + id));
    }

    public Cliente guardar(Cliente cliente) {
        cliente.setId(null);
        cliente.setRol("CLIENTE");
        cliente.setEmpresa(resolverEmpresa(cliente.getEmpresa()));
        return clienteRepository.save(cliente);
    }

    public Cliente login(String email, String password) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));
        if (!cliente.getPassword().equals(password)) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }
        return cliente;
    }

    public Cliente actualizar(Long id, Cliente clienteNuevo) {
        Cliente clienteGuardado = buscarPorId(id);

        clienteGuardado.setNombre(clienteNuevo.getNombre());
        clienteGuardado.setEmpresa(resolverEmpresa(clienteNuevo.getEmpresa()));
        clienteGuardado.setTelefono(clienteNuevo.getTelefono());
        clienteGuardado.setEmail(clienteNuevo.getEmail());
        if (clienteNuevo.getPassword() != null && !clienteNuevo.getPassword().isBlank()) {
            clienteGuardado.setPassword(clienteNuevo.getPassword());
        }

        return clienteRepository.save(clienteGuardado);
    }

    public void eliminar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

    private Empresa resolverEmpresa(Empresa empresa) {
        if (empresa == null || empresa.getId() == null) {
            return null;
        }
        return empresaRepository.findById(empresa.getId())
                .orElseThrow(() -> new RuntimeException("No existe la empresa con id " + empresa.getId()));
    }
}
