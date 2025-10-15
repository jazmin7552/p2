package com.example.demo.application.service;

import com.example.demo.domain.dto.ProductoDTO;
import com.example.demo.domain.entity.Categoria;
import com.example.demo.domain.entity.Producto;
import com.example.demo.domain.mapper.ProductoMapper;
import com.example.demo.infrastructure.persistence.repository.CategoriaRepository;
import com.example.demo.infrastructure.persistence.repository.ProductoRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.InsufficientStockException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        log.info("Obteniendo todos los productos");
        List<Producto> productos = productoRepository.findAll();
        log.info("Se encontraron {} productos", productos.size());
        return productoMapper.toDtoList(productos);
    }

    @Transactional(readOnly = true)
    public ProductoDTO findById(Integer id) {
        log.info("Buscando producto con ID: {}", id);
        if (id == null || id <= 0)
            throw new BadRequestException("El ID del producto debe ser un número positivo");

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return productoMapper.toDto(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> findByCategoria(Integer idCategoria) {
        log.info("Buscando productos de la categoría: {}", idCategoria);
        if (idCategoria == null || idCategoria <= 0)
            throw new BadRequestException("El ID de la categoría debe ser un número positivo");
        if (!categoriaRepository.existsById(idCategoria))
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + idCategoria);
        List<Producto> productos = productoRepository.findByCategoriaIdCategoria(idCategoria);
        return productoMapper.toDtoList(productos);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> findByEstado(Boolean estado) {
        log.info("Buscando productos por estado: {}", estado);
        if (estado == null)
            throw new BadRequestException("El estado no puede ser nulo");
        List<Producto> productos = productoRepository.findByEstado(estado);
        return productoMapper.toDtoList(productos);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> searchByNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);
        if (nombre == null || nombre.trim().isEmpty())
            throw new BadRequestException("El nombre de búsqueda no puede estar vacío");
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre.trim());
        return productoMapper.toDtoList(productos);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> findByStockDisponible(Integer stockMinimo) {
        log.info("Buscando productos con stock mayor a: {}", stockMinimo);
        if (stockMinimo == null || stockMinimo < 0)
            throw new BadRequestException("El stock mínimo debe ser mayor o igual a 0");
        List<Producto> productos = productoRepository.findByStockGreaterThan(stockMinimo);
        return productoMapper.toDtoList(productos);
    }

    public ProductoDTO save(ProductoDTO productoDTO) {
        log.info("Intentando crear producto: {}", productoDTO.getNombre());
        if (productoDTO.getNombre() == null || productoDTO.getNombre().trim().isEmpty())
            throw new BadRequestException("El nombre del producto es obligatorio");
        if (productoDTO.getCategoriaId() == null)
            throw new BadRequestException("La categoría es obligatoria");
        if (productoDTO.getPrecio() == null || productoDTO.getPrecio().compareTo(BigDecimal.ZERO) <= 0)
            throw new BadRequestException("El precio debe ser mayor a 0");
        if (productoDTO.getStock() == null || productoDTO.getStock() < 0)
            throw new BadRequestException("El stock no puede ser negativo");

        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));

        if (productoDTO.getEstado() == null)
            productoDTO.setEstado(true);

        String nombreNormalizado = productoDTO.getNombre().trim().toUpperCase();
        if (productoRepository.existsByNombreIgnoreCase(nombreNormalizado))
            throw new BadRequestException("Ya existe un producto con el nombre: " + productoDTO.getNombre());

        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setCategoria(categoria);
        producto.setNombre(nombreNormalizado);
        producto.setPrecio(productoDTO.getPrecio());

        Producto saved = productoRepository.save(producto);
        log.info("Producto creado con ID: {}", saved.getIdProducto());
        return productoMapper.toDto(saved);
    }

    public ProductoDTO update(Integer id, ProductoDTO productoDTO) {
        log.info("Intentando actualizar producto ID: {}", id);
        if (id == null || id <= 0)
            throw new BadRequestException("El ID del producto debe ser un número positivo");

        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (productoDTO.getNombre() != null) {
            if (productoDTO.getNombre().trim().isEmpty())
                throw new BadRequestException("El nombre del producto es obligatorio");
            if (productoDTO.getNombre().length() > 50)
                throw new BadRequestException("El nombre no puede exceder 50 caracteres");
            String nombreNormalizado = productoDTO.getNombre().trim().toUpperCase();
            if (!productoExistente.getNombre().equalsIgnoreCase(nombreNormalizado) &&
                    productoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
                throw new BadRequestException("Ya existe un producto con el nombre: " + productoDTO.getNombre());
            }
            productoExistente.setNombre(nombreNormalizado);
        }

        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
            productoExistente.setCategoria(categoria);
        }

        if (productoDTO.getPrecio() != null) {
            if (productoDTO.getPrecio().compareTo(BigDecimal.ZERO) <= 0)
                throw new BadRequestException("El precio debe ser mayor a 0");
            productoExistente.setPrecio(productoDTO.getPrecio());
        }

        if (productoDTO.getStock() != null) {
            if (productoDTO.getStock() < 0)
                throw new BadRequestException("El stock no puede ser negativo");
            productoExistente.setStock(productoDTO.getStock());
        }

        if (productoDTO.getEstado() != null)
            productoExistente.setEstado(productoDTO.getEstado());

        Producto updated = productoRepository.save(productoExistente);
        log.info("Producto actualizado: {}", updated.getNombre());
        return productoMapper.toDto(updated);
    }

    public void deleteById(Integer id) {
        log.info("Intentando eliminar producto con ID: {}", id);
        if (id == null || id <= 0)
            throw new BadRequestException("El ID del producto debe ser un número positivo");

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (producto.getDetallesComanda() != null && !producto.getDetallesComanda().isEmpty()) {
            throw new BadRequestException("No se puede eliminar producto asociado a "
                    + producto.getDetallesComanda().size() + " comanda(s).");
        }

        productoRepository.deleteById(id);
        log.info("Producto eliminado: {}", producto.getNombre());
    }

    public void reducirStock(Integer idProducto, Integer cantidad) {
        log.info("Reduciendo stock del producto {} en {} unidades", idProducto, cantidad);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + idProducto));

        if (cantidad == null || cantidad <= 0)
            throw new BadRequestException("La cantidad debe ser mayor a 0");
        if (producto.getStock() < cantidad)
            throw new InsufficientStockException("Stock insuficiente");
        if (!producto.getEstado())
            throw new BadRequestException("El producto no está activo");

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
        log.info("Stock actualizado. Nuevo stock: {}", producto.getStock());
    }

    public void aumentarStock(Integer idProducto, Integer cantidad) {
        log.info("Aumentando stock del producto {} en {} unidades", idProducto, cantidad);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + idProducto));

        if (cantidad == null || cantidad <= 0)
            throw new BadRequestException("La cantidad debe ser mayor a 0");

        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
        log.info("Stock actualizado. Nuevo stock: {}", producto.getStock());
    }

    @Transactional
    public ProductoDTO cambiarEstado(Integer id, Boolean estado) {
        if (id == null || id <= 0)
            throw new BadRequestException("El ID del producto debe ser válido");
        if (estado == null)
            throw new BadRequestException("El estado no puede ser nulo");

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setEstado(estado);
        Producto saved = productoRepository.save(producto);
        log.info("Estado actualizado. Producto {} estado={}", saved.getIdProducto(), saved.getEstado());
        return productoMapper.toDto(saved);
    }
}
