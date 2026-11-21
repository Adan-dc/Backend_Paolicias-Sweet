package com.microservicios.microservicio_reservas.model;

import lombok.Data;

@Data
public class ProductoDetalle {

    private Long id;
    private String nombre;
    private Integer precioBase;

}
