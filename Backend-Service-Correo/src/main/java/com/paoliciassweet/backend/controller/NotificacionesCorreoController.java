package com.paoliciassweet.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.paoliciassweet.backend.model.NotificacionesCorreo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.paoliciassweet.backend.service.NotificacionesCorreoService;

@RestController 
@RequestMapping ("/api/v1/correos")
public class NotificacionesCorreoController {
    @Autowired
    private NotificacionesCorreoService notificacionesCorreoService;
    @Operation(summary = "Obtener todo los correos", description = "Retorna una lista completa de los corres de la base de datos db_correos")
    @GetMapping
    public List<NotificacionesCorreo> listar(){
        return notificacionesCorreoService.listarTodos();
    }
    @Operation(summary = "Obtener por Id")
    @ApiResponses (value = {
        @ApiResponse(responseCode = "200", description = "Correo encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe el correo que busca", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionesCorreo> obtener(@PathVariable Long id) {
        return notificacionesCorreoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Crea un nuevo Correo", description = "Manda un correo al cliente  y se guarda dentro de la base de datos db_correos")
    @PostMapping("/email")
    public NotificacionesCorreo enviarEmail(@RequestBody Map<String, Object> datos) {

        Long idCliente = Long.valueOf(datos.get("idCliente").toString());
        String asunto = datos.get("asunto").toString();
        String mensaje = datos.get("mensaje").toString();

        return notificacionesCorreoService.enviarCorreo(
                idCliente,
                asunto,
                mensaje
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        notificacionesCorreoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
