package com.service.costo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.service.costo.model.CostoCalculo;

public interface CostoCalculoRepository extends JpaRepository<CostoCalculo, Long>{

    Optional<CostoCalculo> findByIdReserva(Long idReserva);
}
