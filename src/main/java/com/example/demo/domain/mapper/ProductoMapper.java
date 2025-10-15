package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.ProductoDTO;
import com.example.demo.domain.entity.Producto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    // ENTITY -> DTO (precio se mapea directamente porque ambos son Double)
    @Mapping(source = "categoria.idCategoria", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    ProductoDTO toDto(Producto entity);

    List<ProductoDTO> toDtoList(List<Producto> entities);

    // DTO -> ENTITY (precio mapeado directamente)
    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "categoria", ignore = true) // se establece en Service
    @Mapping(target = "detallesComanda", ignore = true)
    Producto toEntity(ProductoDTO dto);

    // Actualizar entidad desde DTO
    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "detallesComanda", ignore = true)
    void updateEntityFromDto(ProductoDTO dto, @MappingTarget Producto entity);
}
