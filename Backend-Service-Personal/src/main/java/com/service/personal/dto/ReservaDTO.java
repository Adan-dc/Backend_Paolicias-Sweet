package com.service.personal.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@AllArgsConstructor   
@NoArgsConstructor  
public class ReservaDTO {
    private Long id;
    private Date fechaEvento;
    private LocalDate fecha;
}
