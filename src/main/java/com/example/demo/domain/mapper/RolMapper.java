package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.RolDTO;
import com.example.demo.domain.entity.Rol; 
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RolMapper {

    @Mapping(target = "idRol", source = "idRol") 
    RolDTO toDto(Rol entity);

    @Mapping(target = "idRol", source = "idRol") 
    @Mapping(target = "usuarios", ignore = true)
    Rol toEntity(RolDTO dto);

    @Mapping(target = "idRol", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    void updateEntityFromDto(RolDTO dto, @MappingTarget Rol entity);

    java.util.List<RolDTO> toDtoList(java.util.List<Rol> entities);
}