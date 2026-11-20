package com.microservicios.microservicio_pago.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservicios.microservicio_pago.model.Pago;
import com.microservicios.microservicio_pago.repository.PagoRepository;
import com.microservicios.microservicio_pago.model.ReservaDetalle;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    
    private final RestClient restClient =
            RestClient.create("http://localhost:8081");



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
            throw new IllegalArgumentException(
                "El pago debe estar asociado a una reserva"
            );
        }

        if (!"ABONO".equals(pago.getTipoPago()) && !"PAGO".equals(pago.getTipoPago())) {
            throw new IllegalArgumentException(
                "El tipo de pago debe ser ABONO o PAGO"
            );
        }


        if (!"TRANSFERENCIA".equals(pago.getMetodoPago())) {
            throw new IllegalArgumentException(
                "El unico metodo de pago permitido es TRANSFERENCIA"
            );
        }


        
        ReservaDetalle reserva = restClient.get()
            .uri("/api/reservas/{id}", pago.getReservaId())
            .retrieve()
            .body(ReservaDetalle.class);
        if (reserva == null) {
            throw new IllegalArgumentException(
                "La reserva no existe"
            );
        }

        if ("CANCELADA".equals(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException(
                "No se puede pagar una reserva cancelada"
            );
        }

        if (reserva.getPresupuestoTotal() <= 0) {
            throw new IllegalArgumentException(
                "La reserva no tiene un presupuesto valido"
            );
        }

        int abono = reserva.getPresupuestoTotal() / 2;

        int montoEsperado;

        if ("ABONO".equals(pago.getTipoPago())) {
            montoEsperado = abono;
        } else {
            montoEsperado = reserva.getPresupuestoTotal() - abono;
        }

        if (pago.getMonto() != montoEsperado) {
            throw new IllegalArgumentException(
                "El monto correcto para " + pago.getTipoPago()
                + " es: " + montoEsperado
            );
        }


        boolean pagoExistente =
        pagoRepository.existsByReservaIdAndTipoPagoAndEstadoPagoIn(
            pago.getReservaId(),
            pago.getTipoPago(),
            List.of("PENDIENTE", "APROBADO")
        );

    if (pagoExistente) {
        throw new IllegalArgumentException(
            "Ya existe un pago pendiente o aprobado de este tipo"
        );
    }

    if ("PAGO".equals(pago.getTipoPago())) {

        boolean abonoAprobado =
            pagoRepository.existsByReservaIdAndTipoPagoAndEstadoPago(
                pago.getReservaId(),
                "ABONO",
                "APROBADO"
            );

        if (!abonoAprobado) {
            throw new IllegalArgumentException(
                "Primero debe aprobarse el abono"
            );
        }
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
        if ("ABONO".equals(pago.getTipoPago())) {
            restClient.put()
                    .uri("http://localhost:8081/api/reservas/{id}/aprobar-abono",
                            pago.getReservaId())
                    .retrieve()
                    .toBodilessEntity();
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
