package com.service.personal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.personal.dto.ReservaDTO;
import com.service.personal.model.Trabajador;
import com.service.personal.repository.TrabajadorRepository;

import jakarta.transaction.Transactional;

@Service 
public class TrabajadorService {
    @Autowired 
    private TrabajadorRepository trabajadorRepository;
    @Autowired 
    private WebClient.Builder webClientBuilder;

     
    public List<Trabajador> listarTodos() {

        List<Trabajador> trabajadores =
                trabajadorRepository.findAll();
    
        for (Trabajador trabajador : trabajadores) {
    
            revisarDisponibilidad(trabajador);
        }
    
        return trabajadores;
    }

    public Trabajador cancelarAsignacion(Long idTrabajador) {

        Trabajador trabajador = trabajadorRepository.findById(idTrabajador)
                .orElseThrow(() ->
                        new IllegalArgumentException("No se encontró el trabajador")
                );
    
        trabajador.setEstado(false);
        trabajador.setIdReservaAsignada(null);
    
        return trabajadorRepository.save(trabajador);
    }

    private void liberarTrabajador(Trabajador trabajador) {

        trabajador.setEstado(false);
        trabajador.setIdReservaAsignada(null);
    
        trabajadorRepository.save(trabajador);
    }

    private void revisarDisponibilidad(Trabajador trabajador) {

        if (!trabajador.getEstado()
                || trabajador.getIdReservaAsignada() == null) {
            return;
        }
    
        try {
    
            ReservaDTO reserva = webClientBuilder.build()
                    .get()
                    .uri(
                            "http://localhost:8081/api/reservas/"
                                    + trabajador.getIdReservaAsignada()
                    )
                    .retrieve()
                    .bodyToMono(ReservaDTO.class)
                    .block();
    
            if (reserva == null) {
                liberarTrabajador(trabajador);
                return;
            }
    
            if (reserva.getFecha().isBefore(LocalDate.now())) {
                liberarTrabajador(trabajador);
                return;
            }
    
        } catch (Exception e) {
    
            liberarTrabajador(trabajador);
        }
    }

    public Trabajador asignacionTrabajador(Long idReserva, Long idTrabajador){

            if (idReserva == null || idTrabajador == null) {
                throw new IllegalArgumentException("Los ID no pueden ser nulos");
            }
                
            Trabajador trabajadorAntiguo = trabajadorRepository.findById(idTrabajador).orElseThrow(() -> new IllegalArgumentException("No se encontró el trabajador"));

            ReservaDTO reserva = webClientBuilder.build()
            .get()
            .uri("http://localhost:8081/api/reservas/" + idReserva)
            .retrieve()
            .bodyToMono(ReservaDTO.class)
            .block();

            if (reserva == null || !reserva.getId().equals(idReserva)) {
                throw new IllegalArgumentException("No se encontro la reserva");
            }

            if (trabajadorAntiguo.getEstado()) {
                throw new IllegalArgumentException("No se encuentra disponible el trabajador");
            }

            trabajadorAntiguo.setEstado(true);
            trabajadorAntiguo.setIdReservaAsignada(idReserva);

            return trabajadorRepository.save(trabajadorAntiguo);
    }

    @Transactional 
    public Trabajador crearTrabajador(Trabajador trabajador){
        return trabajadorRepository.save(trabajador);
    }

    public Trabajador actualizarTrabajador(Long id, Trabajador trabajadorNuevo) {

        Optional<Trabajador> trabajador = trabajadorRepository.findById(id);

        if (trabajador.isPresent()) {

            Trabajador trabajadorGuardado = trabajador.get();

            trabajadorGuardado.setNombreCompleto(trabajadorNuevo.getNombreCompleto());
            trabajadorGuardado.setRut(trabajadorNuevo.getRut());
            trabajadorGuardado.setCorreo(trabajadorNuevo.getCorreo());
            trabajadorGuardado.setDireccion(trabajadorNuevo.getDireccion());

            return trabajadorRepository.save(trabajadorGuardado);
        }
        return null; 
    }
    
    public void eliminar(Long id) {
        trabajadorRepository.deleteById(id);
    }
}
