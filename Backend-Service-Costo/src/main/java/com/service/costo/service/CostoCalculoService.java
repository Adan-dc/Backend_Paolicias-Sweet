package com.service.costo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.costo.dto.DetalleCostoReservaDTO;
import com.service.costo.dto.IngredienteDTO;
import com.service.costo.dto.ProductoDTO;
import com.service.costo.dto.ReservaDTO;
import com.service.costo.model.CostoCalculo;
import com.service.costo.repository.CostoCalculoRepository;

import java.util.ArrayList;

import com.service.costo.dto.DetalleCostoReservaDTO;
import com.service.costo.dto.IngredienteCostoDTO;
import com.service.costo.dto.ProductoCostoDTO;

@Service 
public class CostoCalculoService {
    @Autowired 
    private CostoCalculoRepository costoCalculoRepository;
    @Autowired 
    private WebClient.Builder webClientBuilder;

    public CostoCalculo calcularCosto(Long idReserva) {

        ReservaDTO reserva = webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/api/reservas/" + idReserva)
                .retrieve()
                .bodyToMono(ReservaDTO.class)
                .block();
    
        if (reserva == null) {
            throw new IllegalArgumentException(
                    "No se encontró la reserva"
            );
        }
    
    
        List<DetalleCostoReservaDTO> detalles = webClientBuilder.build()
                .get()
                .uri(
                        "http://localhost:8081/api/reservas/"
                                + idReserva
                                + "/productos"
                )
                .retrieve()
                .bodyToFlux(DetalleCostoReservaDTO.class)
                .collectList()
                .block();
    
    
        if (detalles == null || detalles.isEmpty()) {
    
            throw new IllegalArgumentException(
                    "La reserva no tiene productos asociados"
            );
        }
    
    
        double costoTotal = 0;
    
    
        for (DetalleCostoReservaDTO detalle : detalles) {
    
            ProductoDTO producto = webClientBuilder.build()
                    .get()
                    .uri(
                            "http://localhost:8085/api/v1/producto/"
                                    + detalle.getProductoId()
                    )
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block();
    
    
            if (producto == null) {
    
                throw new IllegalArgumentException(
                        "No se encontró el producto "
                                + detalle.getProductoId()
                );
            }
    
    
            for (IngredienteDTO ingrediente : producto.getIngredientes()) {
    
                costoTotal +=
                        ingrediente.getCantidad()
                                * ingrediente.getIngrediente().getPrecioUnidad()
                                * detalle.getCantidad();
            }
        }
    
    
        CostoCalculo calculo = new CostoCalculo();
    
        calculo.setIdReserva(idReserva);
    
        calculo.setCantidadProductosTotal(
                reserva.getCantidadProductosTotal()
        );
    
        calculo.setCostoTotal(costoTotal);
    
    
        return costoCalculoRepository.save(calculo);
    }

    public List<ProductoCostoDTO> obtenerDetalleCosto(Long idReserva) {

        List<DetalleCostoReservaDTO> detalles = webClientBuilder.build()
                .get()
                .uri(
                        "http://localhost:8081/api/reservas/"
                                + idReserva
                                + "/productos"
                )
                .retrieve()
                .bodyToFlux(DetalleCostoReservaDTO.class)
                .collectList()
                .block();
    
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "La reserva no tiene productos asociados"
            );
        }
    
        List<ProductoCostoDTO> productosCosto =
                new ArrayList<>();
    
        for (DetalleCostoReservaDTO detalle : detalles) {
    
            ProductoDTO producto = webClientBuilder.build()
                    .get()
                    .uri(
                            "http://localhost:8085/api/v1/producto/"
                                    + detalle.getProductoId()
                    )
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block();
    
            if (producto == null) {
                throw new IllegalArgumentException(
                        "No se encontró el producto "
                                + detalle.getProductoId()
                );
            }
    
            List<IngredienteCostoDTO> ingredientesCosto =
                    new ArrayList<>();
    
            double costoTotalProducto = 0;
    
            for (IngredienteDTO ingrediente : producto.getIngredientes()) {
    
                double cantidadTotalIngrediente =
                        ingrediente.getCantidad()
                                * detalle.getCantidad();
    
                double costoIngrediente =
                        cantidadTotalIngrediente
                                * ingrediente
                                .getIngrediente()
                                .getPrecioUnidad();
    
                IngredienteCostoDTO ingredienteCosto =
                        new IngredienteCostoDTO();
    
                ingredienteCosto.setNombre(
                        ingrediente.getIngrediente().getNombre()
                );
    
                ingredienteCosto.setCantidad(
                        cantidadTotalIngrediente
                );
    
                ingredienteCosto.setUnidadMedida(
                        ingrediente.getIngrediente().getUnidadMedida()
                );
    
                ingredienteCosto.setPrecioUnidad(
                        ingrediente.getIngrediente().getPrecioUnidad()
                );
    
                ingredienteCosto.setCosto(
                        costoIngrediente
                );
    
                ingredientesCosto.add(
                        ingredienteCosto
                );
    
                costoTotalProducto += costoIngrediente;
            }
    
            ProductoCostoDTO productoCosto =
                    new ProductoCostoDTO();
    
            productoCosto.setProductoId(
                    detalle.getProductoId()
            );
    
            productoCosto.setNombreProducto(
                    detalle.getNombreProducto()
            );
    
            productoCosto.setCantidad(
                    detalle.getCantidad()
            );
    
            productoCosto.setIngredientes(
                    ingredientesCosto
            );
    
            productoCosto.setCostoTotal(
                    costoTotalProducto
            );
    
            productosCosto.add(
                    productoCosto
            );
        }
    
        return productosCosto;
    }

    public List<CostoCalculo> listarTodos(){
        return costoCalculoRepository.findAll();
    }

    public void eliminar(Long id) {
        costoCalculoRepository.deleteById(id);
    }

    
}
