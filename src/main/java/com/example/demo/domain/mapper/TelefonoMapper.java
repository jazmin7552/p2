package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.TelefonoDTO;
import com.example.demo.domain.entity.Telefono;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TelefonoMapper {

    TelefonoDTO toDto(Telefono telefono);

    @Mapping(target = "usuarios", ignore = true)
    Telefono toEntity(TelefonoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idTelefono", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    void updateEntityFromDto(TelefonoDTO dto, @MappingTarget Telefono telefono);

    List<TelefonoDTO> toDtoList(List<Telefono> telefonos);
}
