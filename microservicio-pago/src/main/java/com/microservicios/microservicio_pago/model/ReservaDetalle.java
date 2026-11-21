package com.microservicios.microservicio_pago.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservaDetalle {

    private Long id;

    private String estadoReserva;

    private int presupuestoTotal;

}

