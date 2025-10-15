package com.example.demo.application.service;

import com.example.demo.domain.dto.CategoriaDTO;
import com.example.demo.domain.entity.Categoria;
import com.example.demo.domain.mapper.CategoriaMapper;
import com.example.demo.infrastructure.persistence.repository.CategoriaRepository;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    /**
     * Obtener todas las categorías
     */
    @Transactional(readOnly = true)
    public List<CategoriaDTO> findAll() {
        log.info("Obteniendo todas las categorías");
        List<Categoria> categorias = categoriaRepository.findAll();
        log.info("Se encontraron {} categorías", categorias.size());
        return categoriaMapper.toDtoList(categorias);
    }

    /**
     * Buscar categoría por ID
     * VALIDACIÓN: Verifica que exista
     */
    @Transactional(readOnly = true)
    public CategoriaDTO findById(Integer id) {
        log.info("Buscando categoría con ID: {}", id);

        // VALIDACIÓN: ID no puede ser nulo o negativo
        if (id == null || id <= 0) {
            log.error("ID inválido: {}", id);
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo");
        }

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoría no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
                });

        log.info("Categoría encontrada: {}", categoria.getNombre());
        return categoriaMapper.toDto(categoria);
    }

    /**
     * Crear nueva categoría
     * VALIDACIONES:
     * - Nombre no puede estar vacío
     * - Nombre no puede exceder 20 caracteres
     * - Nombre no puede estar duplicado
     */
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        log.info("Intentando crear categoría: {}", categoriaDTO.getNombre());

        // VALIDACIÓN 1: Nombre no puede ser nulo o vacío
        if (categoriaDTO.getNombre() == null || categoriaDTO.getNombre().trim().isEmpty()) {
            log.error("Intento de crear categoría con nombre vacío");
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        // VALIDACIÓN 2: Longitud máxima
        if (categoriaDTO.getNombre().length() > 20) {
            log.error("Nombre demasiado largo: {}", categoriaDTO.getNombre());
            throw new IllegalArgumentException("El nombre de la categoría no puede exceder 20 caracteres");
        }

        // VALIDACIÓN 3: No puede existir otra categoría con el mismo nombre
        String nombreNormalizado = categoriaDTO.getNombre().trim().toUpperCase();
        if (categoriaRepository.existsByNombre(nombreNormalizado)) {
            log.error("Ya existe una categoría con el nombre: {}", categoriaDTO.getNombre());
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        // Normalizar el nombre antes de guardar
        categoriaDTO.setNombre(nombreNormalizado);

        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        Categoria categoriaSaved = categoriaRepository.save(categoria);

        log.info("Categoría creada exitosamente con ID: {}", categoriaSaved.getIdCategoria());
        return categoriaMapper.toDto(categoriaSaved);
    }

    /**
     * Actualizar categoría existente
     * VALIDACIONES:
     * - Categoría debe existir
     * - Nuevo nombre no puede estar vacío
     * - Nuevo nombre no puede estar duplicado (excepto si es el mismo)
     */
    public CategoriaDTO update(Integer id, CategoriaDTO categoriaDTO) {
        log.info("Intentando actualizar categoría ID: {} con nombre: {}", id, categoriaDTO.getNombre());

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para actualización: {}", id);
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo");
        }

        // VALIDACIÓN 2: Categoría debe existir
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoría no encontrada para actualizar con ID: {}", id);
                    return new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
                });

        // VALIDACIÓN 3: Nombre no puede ser vacío
        if (categoriaDTO.getNombre() == null || categoriaDTO.getNombre().trim().isEmpty()) {
            log.error("Intento de actualizar con nombre vacío");
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        // VALIDACIÓN 4: Longitud máxima
        if (categoriaDTO.getNombre().length() > 20) {
            log.error("Nombre demasiado largo en actualización: {}", categoriaDTO.getNombre());
            throw new IllegalArgumentException("El nombre de la categoría no puede exceder 20 caracteres");
        }

        // VALIDACIÓN 5: No puede existir otra categoría con el mismo nombre
        String nombreNormalizado = categoriaDTO.getNombre().trim().toUpperCase();
        if (!categoriaExistente.getNombre().equalsIgnoreCase(nombreNormalizado)) {
            if (categoriaRepository.existsByNombre(nombreNormalizado)) {
                log.error("Ya existe otra categoría con el nombre: {}", categoriaDTO.getNombre());
                throw new IllegalArgumentException(
                        "Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
            }
        }

        // Actualizar solo el nombre (normalizado)
        categoriaExistente.setNombre(nombreNormalizado);
        Categoria categoriaUpdated = categoriaRepository.save(categoriaExistente);

        log.info("Categoría actualizada exitosamente: {}", categoriaUpdated.getNombre());
        return categoriaMapper.toDto(categoriaUpdated);
    }

    /**
     * Eliminar categoría
     * VALIDACIONES:
     * - Categoría debe existir
     * - No puede tener productos asociados
     */
    public void deleteById(Integer id) {
        log.info("Intentando eliminar categoría con ID: {}", id);

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para eliminación: {}", id);
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo");
        }

        // VALIDACIÓN 2: Categoría debe existir
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoría no encontrada para eliminar con ID: {}", id);
                    return new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
                });

        // VALIDACIÓN 3: No puede tener productos asociados
        if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
            log.error("No se puede eliminar categoría con productos asociados. ID: {}", id);
            throw new IllegalStateException(
                    "No se puede eliminar la categoría porque tiene " +
                            categoria.getProductos().size() + " producto(s) asociado(s). " +
                            "Elimine o reasigne los productos primero.");
        }

        categoriaRepository.deleteById(id);
        log.info("Categoría eliminada exitosamente: {}", categoria.getNombre());
    }

    /**
     * Buscar por nombre exacto
     */
    @Transactional(readOnly = true)
    public CategoriaDTO findByNombre(String nombre) {
        log.info("Buscando categoría por nombre: {}", nombre);

        // VALIDACIÓN: Nombre no puede ser vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            log.error("Intento de buscar con nombre vacío");
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }

        Categoria categoria = categoriaRepository.findByNombre(nombre.trim().toUpperCase())
                .orElseThrow(() -> {
                    log.error("Categoría no encontrada con nombre: {}", nombre);
                    return new ResourceNotFoundException("Categoría no encontrada: " + nombre);
                });

        log.info("Categoría encontrada por nombre: {}", categoria.getNombre());
        return categoriaMapper.toDto(categoria);
    }

    /**
     * Verificar si existe por nombre
     */
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        log.info("Verificando existencia de categoría: {}", nombre);

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        boolean exists = categoriaRepository.existsByNombre(nombre.trim().toUpperCase());
        log.info("Categoría '{}' existe: {}", nombre, exists);
        return exists;
    }
}