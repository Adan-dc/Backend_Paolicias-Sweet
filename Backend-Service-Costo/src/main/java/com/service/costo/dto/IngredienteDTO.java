package com.service.costo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@AllArgsConstructor 
@NoArgsConstructor 
public class IngredienteDTO {
    private Long id;
    private IngredienteDetalleDTO ingrediente;
    private Double cantidad;
}
