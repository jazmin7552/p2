package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.EstadoDTO;
import com.example.demo.domain.entity.Estado;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    @Mapping(target = "idEstado", source = "idEstado") 
    EstadoDTO toDto(Estado entity);

    @Mapping(target = "idEstado", source = "idEstado") 
    @Mapping(target = "mesas", ignore = true)
    @Mapping(target = "comandas", ignore = true)
    Estado toEntity(EstadoDTO dto);

    @Mapping(target = "idEstado", ignore = true)
    @Mapping(target = "mesas", ignore = true)
    @Mapping(target = "comandas", ignore = true)
    void updateEntityFromDto(EstadoDTO dto, @MappingTarget Estado entity);

    java.util.List<EstadoDTO> toDtoList(java.util.List<Estado> entities);
}
