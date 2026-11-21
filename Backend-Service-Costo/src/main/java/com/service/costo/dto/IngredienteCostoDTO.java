package com.service.costo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class IngredienteCostoDTO {
    private String nombre;
    private Double cantidad;
    private String unidadMedida;
    private Double precioUnidad;
    private Double costo;
}
