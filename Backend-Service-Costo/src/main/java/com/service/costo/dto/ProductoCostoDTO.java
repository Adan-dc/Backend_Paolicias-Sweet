package com.service.costo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductoCostoDTO {
    private Long productoId;

    private String nombreProducto;
    private Integer cantidad;

    private List<IngredienteCostoDTO> ingredientes;
    
    private Double costoTotal;
}
