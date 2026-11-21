package com.paoliciassweet.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paoliciassweet.backend.model.NotificacionesCorreo;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.paoliciassweet.backend.repository.NotificacionesCorreoRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service 
public class NotificacionesCorreoService {
    @Autowired
    private NotificacionesCorreoRepository notificacionesCorreoRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private JavaMailSender mailSender;

    public List<NotificacionesCorreo> listarTodos(){
        return notificacionesCorreoRepository.findAll();
    }

    public Optional<NotificacionesCorreo> buscarPorId(Long id) {
        return notificacionesCorreoRepository.findById(id);
    }


    public NotificacionesCorreo enviarCorreo(

            Long idCliente,
            String asunto,
            String mensaje
    ) {
        NotificacionesCorreo historial = new NotificacionesCorreo();

        historial.setReferenciaCliente_id(idCliente);
        historial.setAsunto(asunto);
        historial.setCuerpo_mensaje(mensaje);
        historial.setFecha_envio(LocalDateTime.now());


        try {
            String token = request.getHeader("Authorization");
            Map cliente = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:9090/api/v1/clientes/" + idCliente)
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (cliente == null || cliente.get("email") == null) {
                throw new RuntimeException("Cliente no encontrado o sin correo");
            }

        String correoDestino = cliente.get("email").toString();
        
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(correoDestino);
        email.setSubject(asunto);
        email.setText(mensaje);

        mailSender.send(email);

        historial.setEstado_envio("Enviado");

        } catch (Exception e) {
            historial.setEstado_envio("Fallido");
            e.printStackTrace();
            throw new RuntimeException("Error real: " + e.getMessage());
        }

        return notificacionesCorreoRepository.save(historial);
    }

    public NotificacionesCorreo actualizarCorreo(Long id, NotificacionesCorreo correoNuevo) {
        Optional<NotificacionesCorreo> correo = notificacionesCorreoRepository.findById(id);

        if (correo.isPresent()) {
            NotificacionesCorreo correoGuardado = correo.get();

            correoGuardado.setAsunto(correoNuevo.getAsunto());
            correoGuardado.setCuerpo_mensaje(correoNuevo.getCuerpo_mensaje());
            correoGuardado.setFecha_envio(correoNuevo.getFecha_envio());
            correoGuardado.setEstado_envio(correoNuevo.getEstado_envio());
            return notificacionesCorreoRepository.save(correoGuardado);
        }
        
        return null; 
    }
    
    public void eliminar(Long id) {
        notificacionesCorreoRepository.deleteById(id);
    }

}
