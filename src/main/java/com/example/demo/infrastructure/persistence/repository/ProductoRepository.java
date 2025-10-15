package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /**
     * Busca productos por categoría
     */
    List<Producto> findByCategoriaIdCategoria(Integer idCategoria);

    /**
     * Busca productos por estado (activo/inactivo)
     */
    List<Producto> findByEstado(Boolean estado);

    /**
     * Busca productos con stock mayor al especificado
     */
    List<Producto> findByStockGreaterThan(Integer stock);

    /**
     * Busca productos cuyo nombre contenga el texto (case insensitive)
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Verifica si existe un producto con el nombre exacto (case insensitive)
     * ESTE ES EL MÉTODO QUE FALTABA ✅
     */
    boolean existsByNombreIgnoreCase(String nombre);
}