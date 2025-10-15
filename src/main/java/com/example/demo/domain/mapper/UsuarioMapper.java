package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.UsuarioDTO;
import com.example.demo.domain.entity.Usuario;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { TelefonoMapper.class })
public interface UsuarioMapper {

    @Mapping(target = "rolId", source = "rol.idRol")
    @Mapping(target = "rolNombre", source = "rol.nombre")
    UsuarioDTO toDto(Usuario usuario);

    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "telefonos", ignore = true)
    @Mapping(target = "comandasComoMesero", ignore = true)
    @Mapping(target = "comandasComoCocinero", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "telefonos", ignore = true)
    @Mapping(target = "comandasComoMesero", ignore = true)
    @Mapping(target = "comandasComoCocinero", ignore = true)
    void updateEntityFromDto(UsuarioDTO dto, @MappingTarget Usuario entity);

    List<UsuarioDTO> toDtoList(List<Usuario> usuarios);
}
