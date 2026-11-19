package com.microservicio.microservicio_comprobante_pago.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservicio.microservicio_comprobante_pago.model.ComprobantePago;
import com.microservicio.microservicio_comprobante_pago.repository.ComprobantePagoRepository;



@Service
public class ComprobantePagoService {

    @Autowired
    private ComprobantePagoRepository comprobantePagoRepository;


    public List<ComprobantePago> listarTodos() {
        return comprobantePagoRepository.findAll();
    }


    public ComprobantePago obtenerComprobante(Long id) {
        return comprobantePagoRepository.findById(id).orElse(null);
    }


    public List<ComprobantePago> buscarPorPago(Long pagoId) {
        return comprobantePagoRepository.findByPagoId(pagoId);
    }


    public List<ComprobantePago> buscarPorEstado(String estadoComprobante) {
        return comprobantePagoRepository
                .findByEstadoComprobante(estadoComprobante);
    }


    @Transactional
    public ComprobantePago guardar(ComprobantePago comprobante) {
        if (comprobante.getPagoId() == null) {
            throw new RuntimeException(
                "El comprobante debe estar asociado a un pago"
            );
        }
        if (comprobante.getRutaArchivo() == null ||
            comprobante.getRutaArchivo().isBlank()) {
            throw new RuntimeException(
                "El comprobante debe tener un archivo asociado"
            );
        }
        comprobante.setId(null);
        comprobante.setEstadoComprobante("PENDIENTE");
        comprobante.setFechaSubida(LocalDateTime.now());
        comprobante.setObservacionAdmin(null);

        return comprobantePagoRepository.save(comprobante);
    }

    @Transactional
    public ComprobantePago aprobarComprobante(Long id) {
        ComprobantePago comprobante = comprobantePagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Comprobante no encontrado con ID: " + id
            ));
        if (!"PENDIENTE".equals(
                comprobante.getEstadoComprobante())) {
            throw new RuntimeException(
                "El comprobante ya fue procesado. Estado actual: "
                + comprobante.getEstadoComprobante()
            );
        }
        comprobante.setEstadoComprobante("APROBADO");

        return comprobantePagoRepository.save(comprobante);
    }


    @Transactional
    public ComprobantePago rechazarComprobante(
            Long id, String observacion) {
        ComprobantePago comprobante = comprobantePagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Comprobante no encontrado con ID: " + id
            ));
        if (!"PENDIENTE".equals(
                comprobante.getEstadoComprobante())) {
            throw new RuntimeException(
                "El comprobante ya fue procesado. Estado actual: "
                + comprobante.getEstadoComprobante()
            );
        }
        if (observacion == null || observacion.isBlank()) {
            throw new RuntimeException(
                "Debes indicar el motivo del rechazo"
            );
        }

        comprobante.setEstadoComprobante("RECHAZADO");
        comprobante.setObservacionAdmin(observacion);
        return comprobantePagoRepository.save(comprobante);
    }


    @Transactional
    public void eliminar(Long id) {
        if (!comprobantePagoRepository.existsById(id)) {
            throw new RuntimeException(
                "No existe un comprobante con ID: " + id
            );
        }
        comprobantePagoRepository.deleteById(id);
    }

    public List<Object[]> resumenPorEstado() {
        return comprobantePagoRepository.resumenPorEstado();
    }
}
