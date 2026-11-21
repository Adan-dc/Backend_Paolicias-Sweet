package com.service.costo.model;

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
public class CostoCalculo {
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idReserva;

    private Integer cantidadProductosTotal;
    private Double costoTotal;
}
