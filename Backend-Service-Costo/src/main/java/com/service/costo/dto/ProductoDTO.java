package com.service.costo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductoDTO {
    private Long id;
    private String nombre;
    private List<IngredienteDTO> ingredientes;
}
