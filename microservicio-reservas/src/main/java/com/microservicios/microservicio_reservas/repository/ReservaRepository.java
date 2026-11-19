package com.microservicios.microservicio_reservas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservicios.microservicio_reservas.model.Reserva;

@Repository 
public interface ReservaRepository extends JpaRepository<Reserva, Long>{

    List<Reserva> findByUsuarioEmail(String usuarioEmail);
    
    List<Reserva> findByEstadoReserva(String estadoReserva);

    List<Reserva> findByFecha(String fecha);

    @Query ("""
                SELECT r.estadoReserva, COUNT(r),
                        SUM(r.presupuestoTotal)
                FROM Reserva r
                GROUP BY r.estadoReserva 
                """)
    List<Object[]> resumenPorEstado();
}
