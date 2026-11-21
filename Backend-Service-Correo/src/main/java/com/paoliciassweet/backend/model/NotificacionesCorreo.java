package com.paoliciassweet.backend.model;

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
@AllArgsConstructor 
@NoArgsConstructor 
public class NotificacionesCorreo {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long referenciaCliente_id;
    private String asunto;
    @Column(length = 1000)
    private String cuerpo_mensaje;
    private LocalDateTime fecha_envio;
    private String estado_envio;
}
