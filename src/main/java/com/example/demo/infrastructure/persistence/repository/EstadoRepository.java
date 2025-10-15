package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    /**
     * Busca estado por nombre exacto (case sensitive)
     */
    Optional<Estado> findByNombre(String nombre);

    /**
     * Busca estado por nombre (case insensitive)
     */
    Optional<Estado> findByNombreIgnoreCase(String nombre);

    /**
     * Verifica si existe estado por nombre exacto
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si existe estado por nombre (case insensitive)
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Busca múltiples estados por lista de nombres
     * Útil para filtrar estados específicos (ej: solo estados de mesas)
     */
    List<Estado> findByNombreIn(List<String> nombres);
}