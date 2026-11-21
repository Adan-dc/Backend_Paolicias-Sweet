package com.microservicios.microservicio_reservas.model;

import java.util.List;

import lombok.Data;

@Data 

public class PackDetalle {

    private Long id;
    private String nombre;
    private Integer precio; 
    private Integer personas; 
    private Boolean activo;
    private List<ItemPack> productos;

    @Data 
public static class ItemPack{
    private ProductoPack producto; 
    private Integer cantidad; 
    private String grupo;
}

@Data 
public static class ProductoPack{
    private Long id;
    private String nombre;
}

}


