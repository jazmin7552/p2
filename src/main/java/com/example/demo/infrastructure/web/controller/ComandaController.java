package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.ComandaService;
import com.example.demo.domain.dto.ComandaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comandas")
@RequiredArgsConstructor
@Tag(name = "Comandas", description = "Gestión de comandas/órdenes del restaurante")
public class ComandaController {

    private final ComandaService comandaService;

    // =========================
    // 🔹 CONSULTAS (GET)
    // =========================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINERO')")
    @Operation(summary = "Listar todas las comandas")
    public ResponseEntity<List<ComandaDTO>> getAll() {
        return ResponseEntity.ok(comandaService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINERO')")
    @Operation(summary = "Obtener comanda por ID")
    public ResponseEntity<ComandaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(comandaService.findById(id));
    }

    @GetMapping("/mesa/{mesaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Obtener comandas por mesa")
    public ResponseEntity<List<ComandaDTO>> getByMesa(@PathVariable Integer mesaId) {
        return ResponseEntity.ok(comandaService.findByMesaId(mesaId));
    }

    @GetMapping("/mesero/{meseroId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Obtener comandas por mesero")
    public ResponseEntity<List<ComandaDTO>> getByMesero(@PathVariable String meseroId) {
        return ResponseEntity.ok(comandaService.findByMeseroId(meseroId));
    }

    @GetMapping("/cocinero/{cocineroId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COCINERO')")
    @Operation(summary = "Obtener comandas por cocinero")
    public ResponseEntity<List<ComandaDTO>> getByCocinero(@PathVariable String cocineroId) {
        return ResponseEntity.ok(comandaService.findByCocineroId(cocineroId));
    }

    @GetMapping("/estado/{estadoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINERO')")
    @Operation(summary = "Obtener comandas por estado")
    public ResponseEntity<List<ComandaDTO>> getByEstado(@PathVariable Integer estadoId) {
        return ResponseEntity.ok(comandaService.findByEstadoId(estadoId));
    }

    @GetMapping("/fecha")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    @Operation(summary = "Obtener comandas por rango de fechas")
    public ResponseEntity<List<ComandaDTO>> getByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(comandaService.findByFechaBetween(inicio, fin));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINERO')")
    @Operation(summary = "Obtener comandas activas (no pagadas)")
    public ResponseEntity<List<ComandaDTO>> getActivas() {
        return ResponseEntity.ok(comandaService.findComandasActivas());
    }

    @GetMapping("/hoy")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    @Operation(summary = "Obtener comandas del día actual")
    public ResponseEntity<List<ComandaDTO>> getHoy() {
        return ResponseEntity.ok(comandaService.findComandasHoy());
    }

    // =========================
    // 🔹 CREACIÓN Y MODIFICACIÓN
    // =========================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Crear una nueva comanda")
    public ResponseEntity<ComandaDTO> create(@Valid @RequestBody ComandaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comandaService.save(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINERO')")
    @Operation(summary = "Actualizar una comanda existente")
    public ResponseEntity<ComandaDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ComandaDTO dto) {
        return ResponseEntity.ok(comandaService.update(id, dto));
    }

    @PatchMapping("/{id}/estado/{estadoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINERO')")
    @Operation(summary = "Cambiar estado de una comanda")
    public ResponseEntity<ComandaDTO> updateEstado(
            @PathVariable Integer id,
            @PathVariable Integer estadoId) {
        return ResponseEntity.ok(comandaService.cambiarEstado(id, estadoId));
    }

    // =========================
    // 🔹 ELIMINACIÓN
    // =========================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar una comanda")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        comandaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
