package com.example.demo.infrastructure.web.controller;

import com.example.demo.domain.dto.MesaDTO;
import com.example.demo.application.service.MesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    // Obtener todas las mesas
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> getAllMesas() {
        List<MesaDTO> mesas = mesaService.findAll();
        return ResponseEntity.ok(mesas);
    }

    // Buscar mesa por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<MesaDTO> getMesaById(@PathVariable Integer id) {
        MesaDTO mesa = mesaService.findById(id);
        return ResponseEntity.ok(mesa);
    }

    // Buscar mesas por estado
    @GetMapping("/estado/{estadoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> getMesasByEstado(@PathVariable Integer estadoId) {
        List<MesaDTO> mesas = mesaService.findByEstado(estadoId);
        return ResponseEntity.ok(mesas);
    }

    // Buscar mesas por ubicación
    @GetMapping("/ubicacion/{ubicacion}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> getMesasByUbicacion(@PathVariable String ubicacion) {
        List<MesaDTO> mesas = mesaService.findByUbicacion(ubicacion);
        return ResponseEntity.ok(mesas);
    }

    // Buscar mesas disponibles
    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> getMesasDisponibles() {
        List<MesaDTO> mesasDisponibles = mesaService.findMesasDisponibles();
        return ResponseEntity.ok(mesasDisponibles);
    }

    // Buscar mesas con capacidad mínima
    @GetMapping("/capacidad/{capacidad}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> getMesasByCapacidadMinima(@PathVariable Integer capacidad) {
        List<MesaDTO> mesas = mesaService.findByCapacidadMinima(capacidad);
        return ResponseEntity.ok(mesas);
    }

    // Crear mesa
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MesaDTO> createMesa(@Valid @RequestBody MesaDTO request) {
        MesaDTO nuevaMesa = mesaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
    }

    // Actualizar mesa
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<MesaDTO> updateMesa(@PathVariable Integer id, @Valid @RequestBody MesaDTO request) {
        MesaDTO mesaActualizada = mesaService.update(id, request);
        return ResponseEntity.ok(mesaActualizada);
    }

    // Eliminar mesa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMesa(@PathVariable Integer id) {
        mesaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
