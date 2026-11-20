package com.microservicios.microservicio_pago.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.microservicios.microservicio_pago.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{

    List<Pago> findByReservaId(Long reservaId);

    List<Pago> findByEstadoPago(String estadoPago);

    List<Pago> findByTipoPago(String tipoPago);


    boolean existsByReservaIdAndTipoPagoAndEstadoPagoIn(
        Long reservaId,
        String tipoPago,
        List<String> estados
    );

    boolean existsByReservaIdAndTipoPagoAndEstadoPago(
        Long reservaId,
        String tipoPago,
        String estadoPago
    );


    @Query("""
            SELECT p.estadoPago, COUNT(p), SUM(p.monto)
            FROM Pago p
            GROUP BY p.estadoPago
            """)
    List<Object[]> resumenPorEstado();
}
