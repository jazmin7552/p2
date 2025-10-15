package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.EstadoService;
import com.example.demo.domain.dto.EstadoDTO;
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
@RequestMapping("/api/estados")
@RequiredArgsConstructor
@Tag(name = "Estados", description = "Gestión de estados de mesas y comandas")
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    @Operation(summary = "Listar todos los estados")
    public ResponseEntity<List<EstadoDTO>> getAllEstados() {
        List<EstadoDTO> estados = estadoService.findAll();
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID")
    public ResponseEntity<EstadoDTO> getEstadoById(@PathVariable Integer id) {
        EstadoDTO estado = estadoService.findById(id);
        return ResponseEntity.ok(estado);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo estado")
    public ResponseEntity<EstadoDTO> createEstado(@Valid @RequestBody EstadoDTO request) {
        EstadoDTO estado = estadoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(estado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar estado")
    public ResponseEntity<EstadoDTO> updateEstado(
            @PathVariable Integer id,
            @Valid @RequestBody EstadoDTO request) {
        EstadoDTO estado = estadoService.update(id, request);
        return ResponseEntity.ok(estado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar estado")
    public ResponseEntity<Void> deleteEstado(@PathVariable Integer id) {
        estadoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}