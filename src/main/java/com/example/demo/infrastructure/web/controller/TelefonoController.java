package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.TelefonoService;
import com.example.demo.domain.dto.TelefonoDTO;
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
@RequestMapping("/api/telefonos")
@RequiredArgsConstructor
@Tag(name = "Teléfonos", description = "Gestión de teléfonos")
public class TelefonoController {

    private final TelefonoService telefonoService;

    @GetMapping
    @Operation(summary = "Listar todos los teléfonos")
    public ResponseEntity<List<TelefonoDTO>> getAllTelefonos() {
        List<TelefonoDTO> telefonos = telefonoService.findAll();
        return ResponseEntity.ok(telefonos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener teléfono por ID")
    public ResponseEntity<TelefonoDTO> getTelefonoById(@PathVariable Integer id) {
        TelefonoDTO telefono = telefonoService.findById(id);
        return ResponseEntity.ok(telefono);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Crear nuevo teléfono")
    public ResponseEntity<TelefonoDTO> createTelefono(@Valid @RequestBody TelefonoDTO request) {
        TelefonoDTO telefono = telefonoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(telefono);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Actualizar teléfono")
    public ResponseEntity<TelefonoDTO> updateTelefono(
            @PathVariable Integer id,
            @Valid @RequestBody TelefonoDTO request) {
        TelefonoDTO telefono = telefonoService.update(id, request);
        return ResponseEntity.ok(telefono);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar teléfono")
    public ResponseEntity<Void> deleteTelefono(@PathVariable Integer id) {
        telefonoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}