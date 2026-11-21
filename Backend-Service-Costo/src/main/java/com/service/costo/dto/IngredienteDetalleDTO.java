package com.service.costo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class IngredienteDetalleDTO {
    private Long id;
    private String nombre;
    private Double precioUnidad;
    private String unidadMedida;
}
