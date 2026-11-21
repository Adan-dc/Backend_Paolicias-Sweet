package com.service.costo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class DetalleCostoReservaDTO {
    private Long id;
    private Long reservaId;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private String grupo;
}
