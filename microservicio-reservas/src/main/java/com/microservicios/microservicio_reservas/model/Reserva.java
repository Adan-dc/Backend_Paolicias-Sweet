package com.microservicios.microservicio_reservas.model;

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

public class Reserva {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private String usuarioEmail;
    private String tipoEvento;
    private String fecha; 
    private String hora; 
    private int asistentes; 
    private Boolean tieneObservaciones;
    
    @Column(length=1000)
    private String observaciones;
    private String estadoReserva; 
    private String estadoAbono;
    private int presupuestoTotal;
}
