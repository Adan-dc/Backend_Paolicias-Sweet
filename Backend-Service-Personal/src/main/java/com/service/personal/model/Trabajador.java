package com.service.personal.model;

import java.util.List;

import com.service.personal.dto.ReservaDTO;

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
public class Trabajador {
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String rut;
    private String correo;
    private String direccion;
    private Boolean estado;

    private Long idReservaAsignada;
    
}
