package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.MesaDTO;
import com.example.demo.domain.entity.Mesa; 
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { EstadoMapper.class })
public interface MesaMapper {

    @Mapping(target = "estadoId", source = "estado.idEstado")
    @Mapping(target = "estadoNombre", source = "estado.nombre")
    MesaDTO toDto(Mesa entity);

    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "comandas", ignore = true)
    Mesa toEntity(MesaDTO dto);

    @Mapping(target = "idMesa", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "comandas", ignore = true)
    void updateEntityFromDto(MesaDTO dto, @MappingTarget Mesa entity);

    java.util.List<MesaDTO> toDtoList(java.util.List<Mesa> entities);
}