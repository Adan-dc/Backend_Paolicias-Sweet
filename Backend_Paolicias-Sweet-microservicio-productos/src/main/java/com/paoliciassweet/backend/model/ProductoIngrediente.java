package com.paoliciassweet.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductoIngrediente {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @ManyToOne 
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    private Double cantidad;
}
