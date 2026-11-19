package com.microservicios.microservicio_reservas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservicios.microservicio_reservas.model.Reserva;
import com.microservicios.microservicio_reservas.repository.ReservaRepository;

import jakarta.transaction.Transactional;

@Service 
public class ReservaService {

    @Autowired 
    private ReservaRepository reservaRepository;

    public List<Reserva> listarTodos(){
        return reservaRepository.findAll();
    }

    public Reserva obtenerReserva(Long id){
        return reservaRepository.findById(id).orElse(null);
    }

    public List<Reserva> buscarPorUsuario(String usuarioEmail){
        return reservaRepository.findByUsuarioEmail(usuarioEmail);
    }

    public List<Reserva> buscarPorFecha(String fecha){
        return reservaRepository.findByFecha(fecha);
    }

    //Todo lo que sea de reservas
    @Transactional 
    public Reserva guardar(Reserva reserva){
        if (reserva.getAsistentes() <=0 || reserva.getAsistentes() > 100){
            throw new RuntimeException(
                "La cantidad de asistentes debe ser entre 1 y 100"
            );
        }

        if(reserva.getPresupuestoTotal()<0) {
            throw new RuntimeException(
                "El presupuesto total debe ser mayor a 0"
            );
        }

        reserva.setId(null);
        reserva.setEstadoReserva("PENDIENTE");
        reserva.setEstadoAbono("SIN_COMPROBANTE");

        return reservaRepository.save(reserva);
    }


    @Transactional 
    public Reserva confirmarReserva(Long id){
        Reserva reserva=reservaRepository.findById(id).orElseThrow(() -> new RuntimeException(
            "Reserva no encontrada con ese ID: "+ id 
        ));

        if(!"PENDIENTE".equals(reserva.getEstadoReserva())){
            throw new RuntimeException(
                "Esta reserva no esta pendiente. Estado actual: " + reserva.getEstadoReserva()
            );
        }

        reserva.setEstadoReserva("CONFIRMADA");

        return reservaRepository.save(reserva);
    }


    @Transactional 
    public Reserva cancelarReserva(Long id){
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new RuntimeException(
            "No se encontro una reserva con ese ID: " + id
        ));
    

    if("CANCELADA".equals(reserva.getEstadoReserva())){
        throw new RuntimeException(
            "La reserva ya se encuentra cancelada"
        );
    }
    reserva.setEstadoReserva("CANCELADA");

    return reservaRepository.save(reserva);
    }
    
    @Transactional 
    public void eliminar(Long id){
        if(!reservaRepository.existsById(id)){
            throw new RuntimeException(
                "No existe una reserva Con el ID: " + id
            );
        }

        reservaRepository.deleteById(id);
    }

    public List<Reserva> buscarPorEstado(String estadoReserva) {
        return reservaRepository.findByEstadoReserva(estadoReserva);
    }

    //para el resumen de las reservas por estado
    public List<Object[]> resumenPorEstado(){
        return reservaRepository.resumenPorEstado();
    }
}
