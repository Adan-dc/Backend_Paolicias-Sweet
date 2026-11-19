package com.microservicios.microservicio_pago.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.microservicios.microservicio_pago.model.Pago;
import com.microservicios.microservicio_pago.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;


    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    public Pago obtenerPago(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public List<Pago> buscarPorReserva(Long reservaId) {
        return pagoRepository.findByReservaId(reservaId);
    }

    public List<Pago> buscarPorEstado(String estadoPago) {
        return pagoRepository.findByEstadoPago(estadoPago);
    }

    public List<Pago> buscarPorTipo(String tipoPago) {
        return pagoRepository.findByTipoPago(tipoPago);
    }


    @Transactional
    public Pago guardar(Pago pago) {
        if (pago.getReservaId() == null) {
            throw new RuntimeException(
                "El pago debe estar asociado a una reserva"
            );
        }
        if (pago.getMonto() <= 0) {
            throw new RuntimeException(
                "El monto del pago debe ser mayor a cero"
            );
        }
        if (!"ABONO".equals(pago.getTipoPago()) &&
            !"PAGO".equals(pago.getTipoPago())) {
            throw new RuntimeException(
                "El tipo de pago debe ser ABONO o PAGO"
            );
        }

        pago.setId(null);
        pago.setEstadoPago("PENDIENTE");
        pago.setFechaPago(LocalDate.now());
        return pagoRepository.save(pago);
    }


    @Transactional
    public Pago aprobarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Pago no encontrado con ID: " + id
            ));
        if (!"PENDIENTE".equals(pago.getEstadoPago())) {
            throw new RuntimeException(
                "El pago ya fue procesado. Estado actual: "
                + pago.getEstadoPago()
            );
        }
        pago.setEstadoPago("APROBADO");
        return pagoRepository.save(pago);
    }


    @Transactional
    public Pago rechazarPago(Long id) {

        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Pago no encontrado con ID: " + id
            ));
        if (!"PENDIENTE".equals(pago.getEstadoPago())) {
            throw new RuntimeException(
                "El pago ya fue procesado. Estado actual: "
                + pago.getEstadoPago()
            );
        }
        pago.setEstadoPago("RECHAZADO");

        return pagoRepository.save(pago);
    }


    @Transactional
    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException(
                "No existe un pago con ID: " + id
            );
        }
        pagoRepository.deleteById(id);
    }


    public List<Object[]> resumenPorEstado() {
        return pagoRepository.resumenPorEstado();
    }
}
