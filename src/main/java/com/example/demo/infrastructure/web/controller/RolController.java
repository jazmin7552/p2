package com.example.demo.infrastructure.web.controller;

import com.example.demo.domain.dto.RolDTO;
import com.example.demo.application.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Roles.
 * Permite CRUD completo:
 * - Listar roles
 * - Buscar por ID o nombre
 * - Crear, actualizar y eliminar
 * 
 * Seguridad: Solo accesible para ADMIN
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    /**
     * Obtener todos los roles
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        List<RolDTO> roles = rolService.findAll();
        return ResponseEntity.ok(roles);
    }

    /**
     * Obtener rol por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTO> getRolById(@PathVariable Integer id) {
        RolDTO rol = rolService.findById(id);
        return ResponseEntity.ok(rol);
    }

    /**
     * Crear un nuevo rol
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTO> createRol(@Valid @RequestBody RolDTO request) {
        RolDTO created = rolService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualizar un rol existente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTO> updateRol(
            @PathVariable Integer id,
            @Valid @RequestBody RolDTO request
    ) {
        RolDTO updated = rolService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminar un rol
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRol(@PathVariable Integer id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar rol por nombre
     */
    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDTO> getRolByNombre(@PathVariable String nombre) {
        RolDTO rol = rolService.findByNombre(nombre);
        return ResponseEntity.ok(rol);
    }
}
