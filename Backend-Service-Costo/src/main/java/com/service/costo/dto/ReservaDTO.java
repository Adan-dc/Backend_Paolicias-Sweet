package com.service.costo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor   
@NoArgsConstructor  
public class ReservaDTO {
    private Long id;
    private Integer cantidadProductosTotal;
}
