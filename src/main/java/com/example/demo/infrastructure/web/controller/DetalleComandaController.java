package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.DetalleComandaService;
import com.example.demo.domain.dto.DetalleComandaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-comanda")
@RequiredArgsConstructor
@Tag(name = "Detalles de Comanda", description = "Gestión de detalles de comandas")
public class DetalleComandaController {

    private final DetalleComandaService detalleComandaService;

    // ============================================================
    // CONSULTAS
    // ============================================================

    @GetMapping
    @Operation(summary = "Listar todos los detalles de comanda")
    public ResponseEntity<?> getAllDetallesComanda() {
        List<DetalleComandaDTO> detalles = detalleComandaService.findAll();
        if (detalles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No hay detalles de comanda registrados.");
        }
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de comanda por ID")
    public ResponseEntity<DetalleComandaDTO> getDetalleComandaById(@PathVariable Integer id) {
        return ResponseEntity.ok(detalleComandaService.findById(id));
    }

    @GetMapping("/comanda/{comandaId}")
    @Operation(summary = "Obtener detalles por ID de comanda")
    public ResponseEntity<?> getDetallesByComanda(@PathVariable Integer comandaId) {
        List<DetalleComandaDTO> detalles = detalleComandaService.findByComandaId(comandaId);
        if (detalles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("La comanda " + comandaId + " no tiene detalles.");
        }
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener detalles por ID de producto")
    public ResponseEntity<?> getDetallesByProducto(@PathVariable Integer productoId) {
        List<DetalleComandaDTO> detalles = detalleComandaService.findByProductoId(productoId);
        if (detalles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("El producto " + productoId + " no aparece en ninguna comanda.");
        }
        return ResponseEntity.ok(detalles);
    }

    // ============================================================
    // CREAR Y ACTUALIZAR
    // ============================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    @Operation(summary = "Crear un nuevo detalle de comanda")
    public ResponseEntity<?> createDetalleComanda(@Valid @RequestBody DetalleComandaDTO request) {
        DetalleComandaDTO creado = detalleComandaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    @Operation(summary = "Actualizar detalle de comanda existente")
    public ResponseEntity<?> updateDetalleComanda(
            @PathVariable Integer id,
            @Valid @RequestBody DetalleComandaDTO request) {

        DetalleComandaDTO actualizado = detalleComandaService.update(id, request);
        return ResponseEntity.ok(actualizado);
    }

    // ============================================================
    //ELIMINAR
    // ============================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un detalle de comanda por ID")
    public ResponseEntity<?> deleteDetalleComanda(@PathVariable Integer id) {
        detalleComandaService.deleteById(id);
        return ResponseEntity.ok("Detalle de comanda con ID " + id + " eliminado correctamente.");
    }

    @DeleteMapping("/comanda/{comandaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar todos los detalles de una comanda")
    public ResponseEntity<?> deleteDetallesByComanda(@PathVariable Integer comandaId) {
        detalleComandaService.deleteByComandaId(comandaId);
        return ResponseEntity.ok("Todos los detalles de la comanda " + comandaId + " fueron eliminados.");
    }
}
