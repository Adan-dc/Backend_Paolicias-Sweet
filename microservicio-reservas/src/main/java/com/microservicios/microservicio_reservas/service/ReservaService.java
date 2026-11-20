package com.microservicios.microservicio_reservas.service;

import java.util.ArrayList;

import org.springframework.web.client.RestClient;

import com.microservicios.microservicio_reservas.model.PackDetalle;
import com.microservicios.microservicio_reservas.model.ReservaProducto;
import com.microservicios.microservicio_reservas.repository.ReservaProductoRepository;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservicios.microservicio_reservas.model.Reserva;
import com.microservicios.microservicio_reservas.repository.ReservaRepository;

import jakarta.transaction.Transactional;

@Service 
public class ReservaService {

    @Autowired
    private ReservaProductoRepository reservaProductoRepository;


    @Autowired 
    private ReservaRepository reservaRepository;
    
    @Value("${microservicio.productos.url}")
    private String productosUrl;

    
    private final RestClient restClient = RestClient.create();



    public List<Reserva> listarTodos(){
        return reservaRepository.findAll();
    }

    public Reserva obtenerReserva(Long id){
        return reservaRepository.findById(id).orElse(null);
    }

    public List<Reserva> buscarPorUsuario(String usuarioEmail){
        return reservaRepository.findByUsuarioEmail(usuarioEmail);
    }

    public List<Reserva> buscarPorFecha(String fecha){
        return reservaRepository.findByFecha(fecha);
    }

    //Todo lo que sea de reservas

    @Transactional
    public Reserva guardar(Reserva reserva) {

        if (reserva.getAsistentes() <= 0 ||
            reserva.getAsistentes() > 100) {
            throw new RuntimeException(
                "La cantidad de asistentes debe estar entre 1 y 100"
            );
        }

        if (reserva.getPackId() == null) {
            throw new RuntimeException(
                "Debes seleccionar un pack"
            );
        }

        
            PackDetalle pack = restClient.get().uri(productosUrl + "/api/v1/pack/{id}",reserva.getPackId())
            .retrieve()
            .body(PackDetalle.class);


        if (pack == null || !Boolean.TRUE.equals(pack.getActivo())) {
            throw new RuntimeException(
                "El pack no existe o no esta disponible"
            );
        }

        if (pack.getPrecio() == null ||
            pack.getPrecio() < 0) {
            throw new RuntimeException(
                "El precio del pack no es valido"
            );
        }

        if (pack.getPersonas() == null ||
            reserva.getAsistentes() > pack.getPersonas()) {
            throw new RuntimeException(
                "El pack no cubre la cantidad de asistentes"
            );
        }

        if (pack.getProductos() == null ||
            pack.getProductos().isEmpty()) {
            throw new RuntimeException(
                "El pack no contiene productos"
            );
        }
        int totalProductos = 0;

        for (PackDetalle.ItemPack item : pack.getProductos()) {

            if (item.getProducto() == null ||
                item.getProducto().getId() == null ||
                item.getCantidad() == null ||
                item.getCantidad() <= 0) {
                throw new RuntimeException(
                    "El pack contiene productos invalidos"
                );
            }

            totalProductos = Math.addExact(
                totalProductos,
                item.getCantidad()
            );
        }

        reserva.setId(null);
        reserva.setEstadoReserva("PENDIENTE");
        reserva.setEstadoAbono("SIN_COMPROBANTE");
        reserva.setPresupuestoTotal(pack.getPrecio());
        reserva.setCantidadProductosTotal(totalProductos);

        Reserva guardada = reservaRepository.save(reserva);

        List<ReservaProducto> productos = new ArrayList<>();

        for (PackDetalle.ItemPack item : pack.getProductos()) {

            ReservaProducto detalle = new ReservaProducto();

            detalle.setReservaId(guardada.getId());
            detalle.setProductoId(item.getProducto().getId());
            detalle.setNombreProducto(item.getProducto().getNombre());
            detalle.setCantidad(item.getCantidad());
            detalle.setGrupo(item.getGrupo());

            productos.add(detalle);
        }

        reservaProductoRepository.saveAll(productos);

        return guardada;
    }

    //

    @Transactional 
    public Reserva confirmarReserva(Long id){
        Reserva reserva=reservaRepository.findById(id).orElseThrow(() -> new RuntimeException(
            "Reserva no encontrada con ese ID: "+ id 
        ));

        if(!"PENDIENTE".equals(reserva.getEstadoReserva())){
            throw new RuntimeException(
                "Esta reserva no esta pendiente. Estado actual: " + reserva.getEstadoReserva()
            );
        }

        reserva.setEstadoReserva("CONFIRMADA");

        return reservaRepository.save(reserva);
    }


    @Transactional 
    public Reserva cancelarReserva(Long id){
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new RuntimeException(
            "No se encontro una reserva con ese ID: " + id
        ));
    

    if("CANCELADA".equals(reserva.getEstadoReserva())){
        throw new RuntimeException(
            "La reserva ya se encuentra cancelada"
        );
    }
    reserva.setEstadoReserva("CANCELADA");

    return reservaRepository.save(reserva);
    }
    
    @Transactional 
    public void eliminar(Long id){
        if(!reservaRepository.existsById(id)){
            throw new RuntimeException(
                "No existe una reserva Con el ID: " + id
            );
        }

        reservaProductoRepository.deleteByReservaId(id);

        reservaRepository.deleteById(id);
    }

    public List<Reserva> buscarPorEstado(String estadoReserva) {
        return reservaRepository.findByEstadoReserva(estadoReserva);
    }

    //para el resumen de las reservas por estado
    public List<Object[]> resumenPorEstado(){
        return reservaRepository.resumenPorEstado();
    }

    public List<ReservaProducto> obtenerProductosReserva(Long reservaId) {
        if(!reservaRepository.existsById(reservaId)){
            throw new RuntimeException(
                "No existe la reserva con el ID: " + reservaId
            );
        }

        return reservaProductoRepository.findByReservaId(reservaId);
    }
}
