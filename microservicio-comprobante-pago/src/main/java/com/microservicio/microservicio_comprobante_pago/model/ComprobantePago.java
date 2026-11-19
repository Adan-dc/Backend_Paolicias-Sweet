package com.microservicio.microservicio_comprobante_pago.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pagoId;
    private String nombreArchivo;
    private String rutaArchivo;
    private String tipoArchivo;
    private LocalDateTime fechaSubida;
    private String estadoComprobante;
    @Column(length = 1000)
    private String observacionAdmin;

}
