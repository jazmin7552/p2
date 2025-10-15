package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.ComandaDTO;
import com.example.demo.domain.entity.Comanda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.List;

@Mapper(componentModel = "spring", uses = { MesaMapper.class, UsuarioMapper.class, EstadoMapper.class,
        DetalleComandaMapper.class })
public interface ComandaMapper {

    @Mapping(target = "mesaId", source = "mesa.idMesa")
    @Mapping(target = "mesaUbicacion", source = "mesa.ubicacion")
    @Mapping(target = "meseroId", source = "mesero.idUsuario")
    @Mapping(target = "meseroNombre", source = "mesero.nombre")
    @Mapping(target = "cocineroId", source = "cocinero.idUsuario")
    @Mapping(target = "cocineroNombre", source = "cocinero.nombre")
    @Mapping(target = "estadoId", source = "estado.idEstado")
    @Mapping(target = "estadoNombre", source = "estado.nombre")
    @Mapping(target = "detalles", source = "detalles")
    @Mapping(target = "total", expression = "java(calcularTotal(entity))")
    ComandaDTO toDto(Comanda entity);

    @Mapping(target = "mesa", ignore = true)
    @Mapping(target = "mesero", ignore = true)
    @Mapping(target = "cocinero", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    Comanda toEntity(ComandaDTO dto);

    @Mapping(target = "idComanda", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "mesa", ignore = true)
    @Mapping(target = "mesero", ignore = true)
    @Mapping(target = "cocinero", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    void updateEntityFromDto(ComandaDTO dto, @org.mapstruct.MappingTarget Comanda entity);

    List<ComandaDTO> toDtoList(List<Comanda> entities);

    // Helper para calcular el total sumando subtotales (asume BigDecimal en
    // DetalleComanda.getSubtotal())
    default BigDecimal calcularTotal(Comanda entity) {
        if (entity == null || entity.getDetalles() == null) {
            return BigDecimal.ZERO;
        }
        return entity.getDetalles().stream()
                .map(d -> d.getSubtotal())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
