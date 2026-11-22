package com.microservicio.microservicio_comprobante_pago.service;

import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



import com.microservicio.microservicio_comprobante_pago.model.ComprobantePago;
import com.microservicio.microservicio_comprobante_pago.repository.ComprobantePagoRepository;
import org.springframework.beans.factory.annotation.Value;


@Service
public class ComprobantePagoService {

    @Autowired
    private ComprobantePagoRepository comprobantePagoRepository;

    private final RestClient pagosClient;
 
    public ComprobantePagoService(@Value("${pagos.base-url}") String pagosBaseUrl) {
    this.pagosClient = RestClient.create(pagosBaseUrl);
    }

    private final Path carpetaArchivos =
            Path.of("uploads", "comprobantes")
                    .toAbsolutePath()
                    .normalize();


    public List<ComprobantePago> listarTodos() {
        return comprobantePagoRepository.findAll();
    }


    public ComprobantePago obtenerComprobante(Long id) {
        return comprobantePagoRepository.findById(id).orElse(null);
    }


    public List<ComprobantePago> buscarPorPago(Long pagoId) {
        return comprobantePagoRepository.findByPagoId(pagoId);
    }


    public List<ComprobantePago> buscarPorEstado(String estadoComprobante) {
        return comprobantePagoRepository
                .findByEstadoComprobante(estadoComprobante);
    }


    @Transactional
    public ComprobantePago guardar(ComprobantePago comprobante) {
        if (comprobante.getPagoId() == null) {
            throw new RuntimeException(
                "El comprobante debe estar asociado a un pago"
            );
        }
        if (comprobante.getRutaArchivo() == null ||
            comprobante.getRutaArchivo().isBlank()) {
            throw new RuntimeException(
                "El comprobante debe tener un archivo asociado"
            );
        }
        comprobante.setId(null);
        comprobante.setEstadoComprobante("PENDIENTE");
        comprobante.setFechaSubida(LocalDateTime.now());
        comprobante.setObservacionAdmin(null);

        return comprobantePagoRepository.save(comprobante);
    }


    @Transactional
    public ComprobantePago subirArchivo(
            Long pagoId, MultipartFile archivo) {

        if (pagoId == null) {
            throw new IllegalArgumentException(
                    "Debes indicar un pago");
        }

        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debes seleccionar un archivo");
        }

        if (archivo.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "El archivo no puede superar los 5 MB");
        }

        String tipo = archivo.getContentType();

        Map<String, String> extensiones = Map.of(
                "application/pdf", ".pdf",
                "image/jpeg", ".jpg",
                "image/png", ".png"
        );

        if (tipo == null || !extensiones.containsKey(tipo)) {
            throw new IllegalArgumentException(
                    "Solo se permiten archivos PDF, JPG o PNG");
        }

        Map<?, ?> pago = pagosClient.get()
                .uri("/api/pagos/{id}", pagoId)
                .retrieve()
                .body(Map.class);

        if (pago == null) {
            throw new IllegalArgumentException(
                    "El pago no existe");
        }

        if (!"ABONO".equals(pago.get("tipoPago"))) {
            throw new IllegalArgumentException(
                    "Solo se aceptan comprobantes de abonos");
        }

        if (!"PENDIENTE".equals(pago.get("estadoPago"))) {
            throw new IllegalArgumentException(
                    "El pago no está pendiente");
        }

        boolean yaExiste = comprobantePagoRepository
                .findByPagoId(pagoId)
                .stream()
                .anyMatch(c ->
                        Set.of("PENDIENTE", "APROBADO")
                                .contains(c.getEstadoComprobante()));

        if (yaExiste) {
            throw new IllegalArgumentException(
                    "Este pago ya tiene un comprobante activo");
        }

        String nombreGuardado =
                UUID.randomUUID() + extensiones.get(tipo);

        Path destino = carpetaArchivos
                .resolve(nombreGuardado)
                .normalize();

        try {
            Files.createDirectories(carpetaArchivos);

            Files.copy(
                    archivo.getInputStream(),
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );

            ComprobantePago comprobante =
                    new ComprobantePago();

            comprobante.setPagoId(pagoId);
            comprobante.setNombreArchivo(
                    archivo.getOriginalFilename());
            comprobante.setRutaArchivo(
                    destino.toString());
            comprobante.setTipoArchivo(tipo);
            comprobante.setFechaSubida(
                    LocalDateTime.now());
            comprobante.setEstadoComprobante("PENDIENTE");
            comprobante.setObservacionAdmin(null);

            try {
                return comprobantePagoRepository
                        .save(comprobante);
            } catch (RuntimeException error) {
                Files.deleteIfExists(destino);
                throw error;
            }

        } catch (IOException error) {
            throw new RuntimeException(
                    "No se pudo guardar el archivo", error);
        }
    }


    
    @Transactional
    public ComprobantePago aprobarComprobante(Long id) {
        ComprobantePago comprobante = comprobantePagoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Comprobante no encontrado con ID: " + id
                ));
        if (!"PENDIENTE".equals(
                comprobante.getEstadoComprobante())) {
            throw new RuntimeException(
                    "El comprobante ya fue procesado. Estado actual: "
                    + comprobante.getEstadoComprobante()
            );
        }
        pagosClient.put()
                .uri("/api/pagos/{id}/aprobar",
                        comprobante.getPagoId())
                .retrieve()
                .toBodilessEntity();
        comprobante.setEstadoComprobante("APROBADO");

        return comprobantePagoRepository.save(comprobante);
    }


    @Transactional
    public ComprobantePago rechazarComprobante(
            Long id, String observacion) {

        ComprobantePago comprobante = comprobantePagoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Comprobante no encontrado con ID: " + id
                ));

        if (!"PENDIENTE".equals(
                comprobante.getEstadoComprobante())) {
            throw new RuntimeException(
                    "El comprobante ya fue procesado. Estado actual: "
                    + comprobante.getEstadoComprobante()
            );
        }
        if (observacion == null || observacion.isBlank()) {
            throw new IllegalArgumentException(
                    "Debes indicar el motivo del rechazo"
            );
        }
        pagosClient.put()
                .uri("/api/pagos/{id}/rechazar",
                        comprobante.getPagoId())
                .retrieve()
                .toBodilessEntity();
        comprobante.setEstadoComprobante("RECHAZADO");
        comprobante.setObservacionAdmin(observacion);

        return comprobantePagoRepository.save(comprobante);
    }



    @Transactional
    public void eliminar(Long id) {
        if (!comprobantePagoRepository.existsById(id)) {
            throw new RuntimeException(
                "No existe un comprobante con ID: " + id
            );
        }
        comprobantePagoRepository.deleteById(id);
    }

    public List<Object[]> resumenPorEstado() {
        return comprobantePagoRepository.resumenPorEstado();
    }



    public byte[] descargarArchivo(Long id) {

        ComprobantePago comprobante =
                comprobantePagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Comprobante no encontrado"
                ));

        try {
            Path archivo = Path.of(comprobante.getRutaArchivo())
                    .toRealPath();

            Path carpetaPermitida =
                    carpetaArchivos.toRealPath();

            if (!archivo.startsWith(carpetaPermitida)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Archivo fuera de la carpeta permitida"
                );
            }

            if (!Files.isRegularFile(archivo)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El archivo no existe"
                );
            }

            return Files.readAllBytes(archivo);

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se pudo encontrar el archivo"
            );
        }
    }

}
