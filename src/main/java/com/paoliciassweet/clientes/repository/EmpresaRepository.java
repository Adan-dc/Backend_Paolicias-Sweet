package com.paoliciassweet.clientes.repository;

import com.paoliciassweet.clientes.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByRazonSocialContainingIgnoreCase(String razonSocial);

    Optional<Empresa> findByRut(String rut);
}