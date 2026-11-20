package com.microservicios.microservicio_reservas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservicios.microservicio_reservas.model.ReservaPack;

@Repository
public interface ReservaPackRepository
        extends JpaRepository<ReservaPack, Long> {

    List<ReservaPack> findByReservaId(Long reservaId);

    void deleteByReservaId(Long reservaId);
}
