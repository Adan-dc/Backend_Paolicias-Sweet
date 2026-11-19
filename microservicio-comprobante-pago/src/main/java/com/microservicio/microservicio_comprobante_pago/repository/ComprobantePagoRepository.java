package com.microservicio.microservicio_comprobante_pago.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservicio.microservicio_comprobante_pago.model.ComprobantePago;


@Repository
public interface ComprobantePagoRepository
        extends JpaRepository<ComprobantePago, Long> {

    List<ComprobantePago> findByPagoId(Long pagoId);

    List<ComprobantePago> findByEstadoComprobante(String estadoComprobante);

    @Query("""
            SELECT c.estadoComprobante, COUNT(c)
            FROM ComprobantePago c
            GROUP BY c.estadoComprobante
            """)
    List<Object[]> resumenPorEstado();
}
