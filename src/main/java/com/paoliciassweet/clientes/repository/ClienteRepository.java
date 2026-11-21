package com.paoliciassweet.clientes.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paoliciassweet.clientes.model.Cliente;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);

    List<Cliente> findByEmpresaId(Long empresaId);
}
