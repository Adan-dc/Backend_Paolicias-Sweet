package com.microservicios.microservicio_reservas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservicios.microservicio_reservas.model.ReservaProducto;

@Repository 

public interface ReservaProductoRepository extends JpaRepository<ReservaProducto, Long>{

    List<ReservaProducto> findByReservaId(Long reservaId);

    void deleteByReservaId(Long reservaId);

}
