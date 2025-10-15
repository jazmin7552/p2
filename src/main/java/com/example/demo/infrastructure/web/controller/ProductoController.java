package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.ProductoService;
import com.example.demo.domain.dto.ProductoDTO;
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
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos del restaurante")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por su ID")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Obtener productos filtrados por categoría")
    public ResponseEntity<List<ProductoDTO>> getProductosByCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(productoService.findByCategoria(categoriaId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener productos por estado (activo/inactivo)")
    public ResponseEntity<List<ProductoDTO>> getProductosByEstado(@PathVariable Boolean estado) {
        return ResponseEntity.ok(productoService.findByEstado(estado));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar productos por nombre (búsqueda parcial)")
    public ResponseEntity<List<ProductoDTO>> searchProductos(@RequestParam("nombre") String nombre) {
        return ResponseEntity.ok(productoService.searchByNombre(nombre));
    }

    @GetMapping("/stock")
    @Operation(summary = "Buscar productos con stock mayor al especificado")
    public ResponseEntity<List<ProductoDTO>> getProductosByStock(
            @RequestParam(defaultValue = "0") Integer minimo) {
        return ResponseEntity.ok(productoService.findByStockDisponible(minimo));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo producto")
    public ResponseEntity<ProductoDTO> createProducto(
            @Valid @RequestBody ProductoDTO request) {
        ProductoDTO producto = productoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar los datos de un producto existente")
    public ResponseEntity<ProductoDTO> updateProducto(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoDTO request) {
        ProductoDTO producto = productoService.update(id, request);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un producto por ID")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- CORRECCIÓN AQUÍ ----------
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar el estado (activo/inactivo) de un producto")
    public ResponseEntity<ProductoDTO> toggleEstadoProducto(
            @PathVariable Integer id,
            @RequestParam Boolean estado) {
        ProductoDTO producto = productoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(producto);
    }
    // --------------------------------------

    @PatchMapping("/{id}/stock/reducir")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLEADO')")
    @Operation(summary = "Reducir el stock de un producto (por ejemplo al vender)")
    public ResponseEntity<Void> reducirStock(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {
        productoService.reducirStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/stock/aumentar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLEADO')")
    @Operation(summary = "Aumentar el stock de un producto")
    public ResponseEntity<Void> aumentarStock(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {
        productoService.aumentarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }
}
