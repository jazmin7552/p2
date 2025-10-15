package com.example.demo.application.service;

import com.example.demo.domain.dto.DetalleComandaDTO;
import com.example.demo.domain.entity.Comanda;
import com.example.demo.domain.entity.DetalleComanda;
import com.example.demo.domain.entity.Producto;
import com.example.demo.domain.mapper.DetalleComandaMapper;
import com.example.demo.infrastructure.persistence.repository.ComandaRepository;
import com.example.demo.infrastructure.persistence.repository.DetalleComandaRepository;
import com.example.demo.infrastructure.persistence.repository.ProductoRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.InsufficientStockException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DetalleComandaService {

    private final DetalleComandaRepository detalleComandaRepository;
    private final DetalleComandaMapper detalleComandaMapper;
    private final ComandaRepository comandaRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<DetalleComandaDTO> findAll() {
        log.info("Obteniendo todos los detalles de comanda");
        List<DetalleComanda> detalles = detalleComandaRepository.findAll();
        return detalleComandaMapper.toDtoList(detalles);
    }

    @Transactional(readOnly = true)
    public DetalleComandaDTO findById(Integer id) {
        if (id == null || id <= 0)
            throw new BadRequestException("El ID del detalle debe ser un número positivo");
        DetalleComanda detalle = detalleComandaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de comanda no encontrado con ID: " + id));
        return detalleComandaMapper.toDto(detalle);
    }

    public DetalleComandaDTO save(DetalleComandaDTO detalleDTO) {
        log.info("Intentando crear detalle de comanda");

        if (detalleDTO.getComandaId() == null)
            throw new BadRequestException("El ID de la comanda es obligatorio");
        Comanda comanda = comandaRepository.findById(detalleDTO.getComandaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comanda no encontrada con ID: " + detalleDTO.getComandaId()));

        if (detalleDTO.getProductoId() == null)
            throw new BadRequestException("El ID del producto es obligatorio");
        Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + detalleDTO.getProductoId()));

        if (!Boolean.TRUE.equals(producto.getEstado()))
            throw new BadRequestException("El producto no está disponible");
        if (detalleDTO.getCantidad() == null || detalleDTO.getCantidad() <= 0)
            throw new BadRequestException("La cantidad debe ser mayor a 0");
        if (detalleDTO.getCantidad() > 100)
            throw new BadRequestException("La cantidad no puede exceder 100 unidades");
        if (producto.getStock() < detalleDTO.getCantidad())
            throw new InsufficientStockException("Stock insuficiente para '" + producto.getNombre() + "'");

        BigDecimal precioUnitario = detalleDTO.getPrecioUnitario() != null
                && detalleDTO.getPrecioUnitario().compareTo(BigDecimal.ZERO) > 0
                        ? detalleDTO.getPrecioUnitario()
                        : producto.getPrecio();

        if (precioUnitario == null)
            throw new BadRequestException("El precio unitario no está definido");

        boolean productoYaExiste = detalleComandaRepository
                .findByComandaIdComanda(detalleDTO.getComandaId())
                .stream()
                .anyMatch(d -> d.getProducto().getIdProducto().equals(detalleDTO.getProductoId()));

        if (productoYaExiste)
            throw new BadRequestException("El producto '" + producto.getNombre() + "' ya está en esta comanda.");

        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleDTO.getCantidad()))
                .setScale(2, RoundingMode.HALF_UP);

        DetalleComanda detalle = detalleComandaMapper.toEntity(detalleDTO);
        detalle.setComanda(comanda);
        detalle.setProducto(producto);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotal);
        detalle.setCantidad(detalleDTO.getCantidad());

        reducirStockProducto(producto, detalleDTO.getCantidad());

        DetalleComanda saved = detalleComandaRepository.save(detalle);
        log.info("Detalle creado: {} x {} = {}", producto.getNombre(), detalleDTO.getCantidad(), subtotal);
        return detalleComandaMapper.toDto(saved);
    }

    public DetalleComandaDTO update(Integer id, DetalleComandaDTO detalleDTO) {
        if (id == null || id <= 0)
            throw new BadRequestException("El ID del detalle debe ser un número positivo");

        DetalleComanda detalleExistente = detalleComandaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de comanda no encontrado con ID: " + id));

        if (detalleDTO.getCantidad() != null) {
            if (detalleDTO.getCantidad() <= 0)
                throw new BadRequestException("La cantidad debe ser mayor a 0");
            if (detalleDTO.getCantidad() > 100)
                throw new BadRequestException("La cantidad no puede exceder 100 unidades");

            Integer cantidadAnterior = detalleExistente.getCantidad();
            Integer diferencia = detalleDTO.getCantidad() - cantidadAnterior;

            if (diferencia > 0) {
                Producto producto = detalleExistente.getProducto();
                if (producto.getStock() < diferencia)
                    throw new InsufficientStockException("Stock insuficiente para aumentar cantidad");
                reducirStockProducto(producto, diferencia);
            } else if (diferencia < 0) {
                aumentarStockProducto(detalleExistente.getProducto(), Math.abs(diferencia));
            }

            detalleExistente.setCantidad(detalleDTO.getCantidad());

            BigDecimal precioUnitario = detalleExistente.getPrecioUnitario();
            if (precioUnitario == null)
                throw new BadRequestException("El precio unitario no está definido en el detalle");

            BigDecimal nuevoSubtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleDTO.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);
            detalleExistente.setSubtotal(nuevoSubtotal);
        }

        DetalleComanda updated = detalleComandaRepository.save(detalleExistente);
        log.info("Detalle actualizado: Cantidad {} - Subtotal {}", updated.getCantidad(), updated.getSubtotal());
        return detalleComandaMapper.toDto(updated);
    }

    public void deleteById(Integer id) {
        if (id == null || id <= 0)
            throw new BadRequestException("El ID del detalle debe ser un número positivo");
        DetalleComanda detalle = detalleComandaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de comanda no encontrado con ID: " + id));

        aumentarStockProducto(detalle.getProducto(), detalle.getCantidad());
        detalleComandaRepository.deleteById(id);
        log.info("Detalle eliminado: {} x {}", detalle.getProducto().getNombre(), detalle.getCantidad());
    }

    @Transactional(readOnly = true)
    public List<DetalleComandaDTO> findByComandaId(Integer idComanda) {
        if (!comandaRepository.existsById(idComanda))
            throw new ResourceNotFoundException("Comanda no encontrada con ID: " + idComanda);
        List<DetalleComanda> detalles = detalleComandaRepository.findByComandaIdComanda(idComanda);
        return detalleComandaMapper.toDtoList(detalles);
    }

    @Transactional(readOnly = true)
    public List<DetalleComandaDTO> findByProductoId(Integer idProducto) {
        if (!productoRepository.existsById(idProducto))
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + idProducto);
        List<DetalleComanda> detalles = detalleComandaRepository.findByProductoIdProducto(idProducto);
        return detalleComandaMapper.toDtoList(detalles);
    }

    public void deleteByComandaId(Integer idComanda) {
        List<DetalleComanda> detalles = detalleComandaRepository.findByComandaIdComanda(idComanda);
        for (DetalleComanda detalle : detalles) {
            aumentarStockProducto(detalle.getProducto(), detalle.getCantidad());
        }
        detalleComandaRepository.deleteByComandaIdComanda(idComanda);
        log.info("Se eliminaron {} detalles de la comanda {}", detalles.size(), idComanda);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalComanda(Integer idComanda) {
        List<DetalleComanda> detalles = detalleComandaRepository.findByComandaIdComanda(idComanda);
        BigDecimal total = detalles.stream()
                .map(DetalleComanda::getSubtotal)
                .filter(s -> s != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        log.info("Total calculado para comanda {}: {}", idComanda, total);
        return total;
    }

    private void reducirStockProducto(Producto producto, Integer cantidad) {
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    private void aumentarStockProducto(Producto producto, Integer cantidad) {
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }
}
