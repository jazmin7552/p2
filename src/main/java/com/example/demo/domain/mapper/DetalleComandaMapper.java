package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.DetalleComandaDTO;
import com.example.demo.domain.entity.DetalleComanda;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para DetalleComanda usando MapStruct
 * Convierte entre Entity y DTO
 */
@Mapper(componentModel = "spring")
public interface DetalleComandaMapper {

    // ============================================
    // ENTITY → DTO (Para respuestas)
    // ============================================

    /**
     * Convierte DetalleComanda Entity a DTO
     * Mapea automáticamente los IDs y nombres
     */
    @Mapping(source = "comanda.idComanda", target = "comandaId")
    @Mapping(source = "producto.idProducto", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    DetalleComandaDTO toDto(DetalleComanda entity);

    /**
     * Convierte lista de Entities a DTOs
     */
    List<DetalleComandaDTO> toDtoList(List<DetalleComanda> entities);

    // ============================================
    // DTO → ENTITY (Para crear/actualizar)
    // ============================================

    /**
     * Convierte DTO a Entity (sin relaciones)
     * Las relaciones (comanda, producto) se establecen manualmente en el Service
     */
    @Mapping(target = "idDetalleComanda", ignore = true)
    @Mapping(target = "comanda", ignore = true) // Se establece en el Service
    @Mapping(target = "producto", ignore = true) // Se establece en el Service
    @Mapping(target = "precioUnitario", ignore = true) // Se calcula en el Service
    @Mapping(target = "subtotal", ignore = true) // Se calcula en el Service
    DetalleComanda toEntity(DetalleComandaDTO dto);

    // ============================================
    // ACTUALIZAR ENTITY DESDE DTO
    // ============================================

    /**
     * Actualiza una entidad existente con datos del DTO
     * Solo actualiza campos permitidos (cantidad)
     */
    @Mapping(target = "idDetalleComanda", ignore = true)
    @Mapping(target = "comanda", ignore = true) // No se puede cambiar
    @Mapping(target = "producto", ignore = true) // No se puede cambiar
    @Mapping(target = "precioUnitario", ignore = true) // No se puede cambiar
    @Mapping(target = "subtotal", ignore = true) // Se recalcula en el Service
    void updateEntityFromDto(DetalleComandaDTO dto, @MappingTarget DetalleComanda entity);
}