package com.service.personal.dto;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@AllArgsConstructor   
@NoArgsConstructor  
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservaDTO {
    private Long id;
    private LocalDate fecha;
}
