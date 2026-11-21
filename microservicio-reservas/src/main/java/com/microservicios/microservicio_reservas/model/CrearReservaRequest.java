package com.microservicios.microservicio_reservas.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearReservaRequest {

    private String usuarioEmail;
    private String tipoEvento;
    private String fecha;
    private String hora;
    private int asistentes;
    private Boolean tieneObservaciones;
    private String observaciones;

    private List<PackSeleccionado> packs;
    private List<ProductoSeleccionado> productos;

    @Data
    @NoArgsConstructor
    public static class PackSeleccionado {
        private Long packId;
        private int cantidad;
    }

    @Data
    @NoArgsConstructor
    public static class ProductoSeleccionado {
        private Long productoId;
        private int cantidad;
    }
}

