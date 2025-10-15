package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.CategoriaDTO;
import com.example.demo.domain.entity.Categoria;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "idCategoria", source = "idCategoria")
    CategoriaDTO toDto(Categoria entity);

    @Mapping(target = "idCategoria", source = "idCategoria")
    @Mapping(target = "productos", ignore = true)
    Categoria toEntity(CategoriaDTO dto);

    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "productos", ignore = true)
    void updateEntityFromDto(CategoriaDTO dto, @MappingTarget Categoria entity);

    List<CategoriaDTO> toDtoList(List<Categoria> entities);
}