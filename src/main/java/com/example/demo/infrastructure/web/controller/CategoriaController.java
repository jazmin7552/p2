package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.CategoriaService;
import com.example.demo.domain.dto.CategoriaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de categorías
 *
 * Permisos:
 * - Lectura (GET): usuarios autenticados (configurado globalmente en
 * SecurityConfig)
 * - Escritura (POST/PUT/DELETE): solo ADMIN
 */
@Slf4j
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Validated
@Tag(name = "Categorías", description = "API para gestión de categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias() {
        log.debug("GET /api/categorias");
        List<CategoriaDTO> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Integer id) {
        log.debug("GET /api/categorias/{}", id);
        CategoriaDTO dto = categoriaService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar categoría por nombre (exacto, case-insensitive)")
    public ResponseEntity<CategoriaDTO> getCategoriaByNombre(@PathVariable String nombre) {
        log.debug("GET /api/categorias/nombre/{}", nombre);
        CategoriaDTO dto = categoriaService.findByNombre(nombre);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/exists/{nombre}")
    @Operation(summary = "Verificar existencia de categoría")
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        log.debug("GET /api/categorias/exists/{}", nombre);
        boolean exists = categoriaService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<CategoriaDTO> createCategoria(
            @Validated(CategoriaDTO.Create.class) @RequestBody CategoriaDTO categoriaDTO) {

        // trim + delegar validación adicional al servicio
        if (categoriaDTO.getNombre() != null) {
            categoriaDTO.setNombre(categoriaDTO.getNombre().trim());
        }

        log.debug("POST /api/categorias - payload: {}", categoriaDTO);
        CategoriaDTO created = categoriaService.save(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar categoría")
    public ResponseEntity<CategoriaDTO> updateCategoria(
            @PathVariable Integer id,
            @Validated(CategoriaDTO.Update.class) @RequestBody CategoriaDTO categoriaDTO) {

        if (categoriaDTO.getNombre() != null) {
            categoriaDTO.setNombre(categoriaDTO.getNombre().trim());
        }

        log.debug("PUT /api/categorias/{} - payload: {}", id, categoriaDTO);
        CategoriaDTO updated = categoriaService.update(id, categoriaDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar categoría")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        log.debug("DELETE /api/categorias/{}", id);
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
