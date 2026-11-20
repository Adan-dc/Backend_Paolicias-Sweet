package com.microservicios.microservicio_reservas.service;

import java.util.ArrayList;

import org.springframework.web.client.RestClient;

import com.microservicios.microservicio_reservas.model.PackDetalle;
import com.microservicios.microservicio_reservas.model.ReservaProducto;
import com.microservicios.microservicio_reservas.repository.ReservaProductoRepository;


import java.util.LinkedHashMap;
import java.util.Map;

import com.microservicios.microservicio_reservas.model.CrearReservaRequest;
import com.microservicios.microservicio_reservas.model.ProductoDetalle;
import com.microservicios.microservicio_reservas.model.ReservaPack;
import com.microservicios.microservicio_reservas.repository.ReservaPackRepository;


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

    @Autowired
    private ReservaPackRepository reservaPackRepository;

    
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
public Reserva guardar(CrearReservaRequest datos) {

    if (datos.getAsistentes() <= 0 ||
        datos.getAsistentes() > 100) {
        throw new RuntimeException(
            "La cantidad de asistentes debe estar entre 1 y 100"
        );
    }

    boolean tienePacks = datos.getPacks() != null &&
                         !datos.getPacks().isEmpty();

    boolean tieneProductos = datos.getProductos() != null &&
                             !datos.getProductos().isEmpty();

    if (!tienePacks && !tieneProductos) {
        throw new RuntimeException(
            "Debes seleccionar al menos un pack o producto"
        );
    }

    int presupuesto = 0;
    int totalProductos = 0;

    List<ReservaPack> packs = new ArrayList<>();

    Map<Long, ReservaProducto> productos = new LinkedHashMap<>();

    // Procesar los packs seleccionados
    if (tienePacks) {

        for (CrearReservaRequest.PackSeleccionado seleccionado
                : datos.getPacks()) {

            if (seleccionado == null ||
                seleccionado.getPackId() == null ||
                seleccionado.getCantidad() <= 0) {
                throw new RuntimeException("Pack invalido");
            }

            PackDetalle pack = restClient.get()
                .uri(productosUrl + "/api/v1/pack/{id}",
                     seleccionado.getPackId())
                .retrieve()
                .body(PackDetalle.class);

            if (pack == null ||
                !Boolean.TRUE.equals(pack.getActivo()) ||
                pack.getPrecio() == null ||
                pack.getPrecio() < 0 ||
                pack.getProductos() == null ||
                pack.getProductos().isEmpty()) {
                throw new RuntimeException(
                    "El pack no esta disponible"
                );
            }

            int subtotal = Math.multiplyExact(
                pack.getPrecio(), seleccionado.getCantidad()
            );

            presupuesto = Math.addExact(presupuesto, subtotal);

            ReservaPack reservaPack = new ReservaPack();
            reservaPack.setPackId(pack.getId());
            reservaPack.setNombrePack(pack.getNombre());
            reservaPack.setCantidad(seleccionado.getCantidad());
            reservaPack.setPrecioUnitario(pack.getPrecio());
            reservaPack.setSubtotal(subtotal);

            packs.add(reservaPack);

            for (PackDetalle.ItemPack item : pack.getProductos()) {

                if (item == null || item.getProducto() == null ||
                    item.getProducto().getId() == null ||
                    item.getCantidad() == null ||
                    item.getCantidad() <= 0) {
                    throw new RuntimeException(
                        "Producto del pack invalido"
                    );
                }

                int cantidad = Math.multiplyExact(
                    item.getCantidad(),
                    seleccionado.getCantidad()
                );

                Long productoId = item.getProducto().getId();

                ReservaProducto detalle = productos.get(productoId);

                if (detalle == null) {
                    detalle = new ReservaProducto();
                    detalle.setProductoId(productoId);
                    detalle.setNombreProducto(item.getProducto().getNombre());
                    detalle.setGrupo(item.getGrupo());
                    detalle.setCantidad(0);
                    productos.put(productoId, detalle);
                }

                detalle.setCantidad(
                    Math.addExact(detalle.getCantidad(), cantidad)
                );
            }
        }
    }

    // Procesar productos individuales
    if (tieneProductos) {

        for (CrearReservaRequest.ProductoSeleccionado seleccionado
                : datos.getProductos()) {

            if (seleccionado == null ||
                seleccionado.getProductoId() == null ||
                seleccionado.getCantidad() <= 0) {
                throw new RuntimeException(
                    "Producto o cantidad invalida"
                );
            }

            ProductoDetalle producto = restClient.get()
                .uri(productosUrl + "/api/v1/producto/{id}",
                     seleccionado.getProductoId())
                .retrieve()
                .body(ProductoDetalle.class);

            if (producto == null ||
                producto.getId() == null ||
                producto.getPrecioBase() == null ||
                producto.getPrecioBase() < 0) {
                throw new RuntimeException(
                    "El producto no esta disponible"
                );
            }

            int subtotal = Math.multiplyExact(
                producto.getPrecioBase(),
                seleccionado.getCantidad()
            );

            presupuesto = Math.addExact(presupuesto, subtotal);

            ReservaProducto detalle = productos.get(producto.getId());

            if (detalle == null) {
                detalle = new ReservaProducto();
                detalle.setProductoId(producto.getId());
                detalle.setNombreProducto(producto.getNombre());
                detalle.setGrupo("Individual");
                detalle.setCantidad(0);
                productos.put(producto.getId(), detalle);
            }

            detalle.setCantidad(
                Math.addExact(
                    detalle.getCantidad(),
                    seleccionado.getCantidad()
                )
            );
        }
    }

    for (ReservaProducto producto : productos.values()) {
        totalProductos = Math.addExact(
            totalProductos, producto.getCantidad()
        );
    }

    // Crear y guardar la reserva
    Reserva reserva = new Reserva();

    reserva.setUsuarioEmail(datos.getUsuarioEmail());
    reserva.setTipoEvento(datos.getTipoEvento());
    reserva.setFecha(datos.getFecha());
    reserva.setHora(datos.getHora());
    reserva.setAsistentes(datos.getAsistentes());
    reserva.setTieneObservaciones(datos.getTieneObservaciones());
    reserva.setObservaciones(datos.getObservaciones());

    reserva.setEstadoReserva("PENDIENTE");
    reserva.setEstadoAbono("SIN_COMPROBANTE");
    reserva.setPresupuestoTotal(presupuesto);
    reserva.setCantidadProductosTotal(totalProductos);

    Reserva guardada = reservaRepository.save(reserva);

    // Guardar los packs asociados
    for (ReservaPack pack : packs) {
        pack.setReservaId(guardada.getId());
    }

    reservaPackRepository.saveAll(packs);

    // Guardar los productos asociados
    for (ReservaProducto producto : productos.values()) {
        producto.setReservaId(guardada.getId());
    }

    reservaProductoRepository.saveAll(productos.values());

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

        reservaPackRepository.deleteByReservaId(id);
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
