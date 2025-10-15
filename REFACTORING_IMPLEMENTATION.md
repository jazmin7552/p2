# 🚀 IMPLEMENTACIÓN COMPLETA DE CLEAN ARCHITECTURE - SISTEMA DE GESTIÓN DE RESTAURANTE

## 📋 FASE 1 - DTOs CONSOLIDADOS COMPLETOS

### 1. UsuarioDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String idUsuario;

    @NotBlank(message = "El nombre es obligatorio", groups = {Create.class, Update.class})
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio", groups = {Create.class, Update.class})
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = {Create.class})
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres", groups = {Create.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "El rol es obligatorio", groups = {Create.class, Update.class})
    private Integer rolId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String rolNombre;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TelefonoDTO> telefonos;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 2. ComandaDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComandaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idComanda;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fecha;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String mesaUbicacion;

    private Integer mesaId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String meseroNombre;

    private String meseroId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String cocineroNombre;

    private String cocineroId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String estadoNombre;

    private Integer estadoId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DetalleComandaDTO> detalles;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer total;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 3. ProductoDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idProducto;

    @NotBlank(message = "El nombre es obligatorio", groups = {Create.class, Update.class})
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotNull(message = "La categoría es obligatoria", groups = {Create.class, Update.class})
    private Integer categoriaId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String categoriaNombre;

    @NotNull(message = "El precio es obligatorio", groups = {Create.class, Update.class})
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Integer precio; // En centavos

    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private Integer stock;

    @NotNull(message = "El estado es obligatorio", groups = {Create.class, Update.class})
    private Boolean estado;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 4. MesaDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MesaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idMesa;

    @NotNull(message = "La capacidad es obligatoria", groups = {Create.class, Update.class})
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidad;

    @NotBlank(message = "La ubicación es obligatoria", groups = {Create.class, Update.class})
    @Size(max = 50, message = "La ubicación no puede exceder 50 caracteres")
    private String ubicacion;

    @NotNull(message = "El estado es obligatorio", groups = {Create.class, Update.class})
    private Integer estadoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String estadoNombre;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 5. RolDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idRol;

    @NotNull(message = "El ID del rol es obligatorio", groups = {Create.class})
    private Integer idRolInput;

    @NotBlank(message = "El nombre del rol es obligatorio", groups = {Create.class, Update.class})
    @Size(max = 20, message = "El nombre del rol no puede exceder 20 caracteres")
    private String nombre;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 6. EstadoDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idEstado;

    @NotNull(message = "El ID del estado es obligatorio", groups = {Create.class})
    private Integer idEstadoInput;

    @NotBlank(message = "El nombre del estado es obligatorio", groups = {Create.class, Update.class})
    @Size(max = 10, message = "El nombre del estado no puede exceder 10 caracteres")
    private String nombre;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 7. CategoriaDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idCategoria;

    @NotNull(message = "El ID de la categoría es obligatorio", groups = {Create.class})
    private Integer idCategoriaInput;

    @NotBlank(message = "El nombre de la categoría es obligatorio", groups = {Create.class, Update.class})
    @Size(max = 20, message = "El nombre de la categoría no puede exceder 20 caracteres")
    private String nombre;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 8. TelefonoDTO (Consolidado)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idTelefono;

    @NotNull(message = "El ID del teléfono es obligatorio", groups = {Create.class})
    private Integer idTelefonoInput;

    @NotBlank(message = "El número de teléfono es obligatorio", groups = {Create.class, Update.class})
    @Size(max = 15, message = "El número de teléfono no puede exceder 15 caracteres")
    @Pattern(regexp = "^[0-9+\\-\\s]+$", message = "El número de teléfono solo puede contener números, espacios, guiones y el símbolo +")
    private String numero;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
```

### 9. DetalleComandaDTO (Como clase interna de ComandaDTO)
```java
package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleComandaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idDetalleComanda;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productoNombre;

    private Integer productoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer precioUnitario;

    private Integer cantidad;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer subtotal;
}
```

## 📋 FASE 2 - MAPPERS MAPSTRUCT COMPLETOS

### 1. UsuarioMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.UsuarioDTO;
import com.example.demo.persistenceLayer.entity.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TelefonoMapper.class, RolMapper.class})
public interface UsuarioMapper {

    @Mapping(target = "rolId", source = "rol.idRol")
    @Mapping(target = "rolNombre", source = "rol.nombre")
    @Mapping(target = "telefonos", source = "telefonos")
    UsuarioDTO toDto(Usuario entity);

    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "telefonos", ignore = true)
    @Mapping(target = "comandasComoMesero", ignore = true)
    @Mapping(target = "comandasComoCocinero", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "telefonos", ignore = true)
    @Mapping(target = "comandasComoMesero", ignore = true)
    @Mapping(target = "comandasComoCocinero", ignore = true)
    void updateEntityFromDto(UsuarioDTO dto, @MappingTarget Usuario entity);

    List<UsuarioDTO> toDtoList(List<Usuario> entities);
}
```

### 2. ComandaMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.ComandaDTO;
import com.example.demo.persistenceLayer.entity.Comanda;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {MesaMapper.class, UsuarioMapper.class, EstadoMapper.class, DetalleComandaMapper.class})
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
    void updateEntityFromDto(ComandaDTO dto, @MappingTarget Comanda entity);

    List<ComandaDTO> toDtoList(List<Comanda> entities);
}
```

### 3. ProductoMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.ProductoDTO;
import com.example.demo.persistenceLayer.entity.Producto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class})
public interface ProductoMapper {

    @Mapping(target = "categoriaId", source = "categoria.idCategoria")
    @Mapping(target = "categoriaNombre", source = "categoria.nombre")
    ProductoDTO toDto(Producto entity);

    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "detallesComanda", ignore = true)
    Producto toEntity(ProductoDTO dto);

    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "detallesComanda", ignore = true)
    void updateEntityFromDto(ProductoDTO dto, @MappingTarget Producto entity);

    List<ProductoDTO> toDtoList(List<Producto> entities);
}
```

### 4. MesaMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.MesaDTO;
import com.example.demo.persistenceLayer.entity.Mesa;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {EstadoMapper.class})
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

    List<MesaDTO> toDtoList(List<Mesa> entities);
}
```

### 5. RolMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.RolDTO;
import com.example.demo.persistenceLayer.entity.Rol;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RolMapper {

    @Mapping(target = "idRolInput", source = "idRol")
    RolDTO toDto(Rol entity);

    @Mapping(target = "idRol", source = "idRolInput")
    @Mapping(target = "usuarios", ignore = true)
    Rol toEntity(RolDTO dto);

    @Mapping(target = "idRol", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    void updateEntityFromDto(RolDTO dto, @MappingTarget Rol entity);

    List<RolDTO> toDtoList(List<Rol> entities);
}
```

### 6. EstadoMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.EstadoDTO;
import com.example.demo.persistenceLayer.entity.Estado;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    @Mapping(target = "idEstadoInput", source = "idEstado")
    EstadoDTO toDto(Estado entity);

    @Mapping(target = "idEstado", source = "idEstadoInput")
    @Mapping(target = "mesas", ignore = true)
    @Mapping(target = "comandas", ignore = true)
    Estado toEntity(EstadoDTO dto);

    @Mapping(target = "idEstado", ignore = true)
    @Mapping(target = "mesas", ignore = true)
    @Mapping(target = "comandas", ignore = true)
    void updateEntityFromDto(EstadoDTO dto, @MappingTarget Estado entity);

    List<EstadoDTO> toDtoList(List<Estado> entities);
}
```

### 7. CategoriaMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.CategoriaDTO;
import com.example.demo.persistenceLayer.entity.Categoria;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "idCategoriaInput", source = "idCategoria")
    CategoriaDTO toDto(Categoria entity);

    @Mapping(target = "idCategoria", source = "idCategoriaInput")
    @Mapping(target = "productos", ignore = true)
    Categoria toEntity(CategoriaDTO dto);

    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "productos", ignore = true)
    void updateEntityFromDto(CategoriaDTO dto, @MappingTarget Categoria entity);

    List<CategoriaDTO> toDtoList(List<Categoria> entities);
}
```

### 8. TelefonoMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.TelefonoDTO;
import com.example.demo.persistenceLayer.entity.Telefono;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TelefonoMapper {

    @Mapping(target = "idTelefonoInput", source = "idTelefono")
    TelefonoDTO toDto(Telefono entity);

    @Mapping(target = "idTelefono", source = "idTelefonoInput")
    @Mapping(target = "usuarioTelefonos", ignore = true)
    Telefono toEntity(TelefonoDTO dto);

    @Mapping(target = "idTelefono", ignore = true)
    @Mapping(target = "usuarioTelefonos", ignore = true)
    void updateEntityFromDto(TelefonoDTO dto, @MappingTarget Telefono entity);

    List<TelefonoDTO> toDtoList(List<Telefono> entities);
}
```

### 9. DetalleComandaMapper
```java
package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.DetalleComandaDTO;
import com.example.demo.persistenceLayer.entity.DetalleComanda;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface DetalleComandaMapper {

    @Mapping(target = "productoId", source = "producto.idProducto")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "precioUnitario", source = "producto.precio")
    DetalleComandaDTO toDto(DetalleComanda entity);

    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "comanda", ignore = true)
    DetalleComanda toEntity(DetalleComandaDTO dto);

    @Mapping(target = "idDetalleComanda", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "comanda", ignore = true)
    void updateEntityFromDto(DetalleComandaDTO dto, @MappingTarget DetalleComanda entity);

    List<DetalleComandaDTO> toDtoList(List<DetalleComanda> entities);
}
```

## 📋 FASE 3 - CONTROLLERS REFACTORIZADOS

### UsuarioController Refactorizado
```java
package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.UsuarioService;
import com.example.demo.domain.dto.UsuarioDTO;
import com.example.demo.domain.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        List<UsuarioDTO> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable String id) {
        UsuarioDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> crear(@Valid @Validated(UsuarioDTO.Create.class) @RequestBody UsuarioDTO dto) {
        var entity = usuarioMapper.toEntity(dto);
        var savedEntity = usuarioService.save(entity);
        var responseDto = usuarioMapper.toDto(savedEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable String id,
                                                @Valid @Validated(UsuarioDTO.Update.class) @RequestBody UsuarioDTO dto) {
        var entity = usuarioService.findEntityById(id);
        usuarioMapper.updateEntityFromDto(dto, entity);
        var updatedEntity = usuarioService.save(entity);
        var responseDto = usuarioMapper.toDto(updatedEntity);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> obtenerPorEmail(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.findByEmail(email);
        return ResponseEntity.ok(usuario);
    }
}
```

### ProductoController Refactorizado
```java
package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.ProductoService;
import com.example.demo.domain.dto.ProductoDTO;
import com.example.demo.domain.mapper.ProductoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Validated
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        List<ProductoDTO> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Integer id) {
        ProductoDTO producto = productoService.findById(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> crear(@Valid @Validated(ProductoDTO.Create.class) @RequestBody ProductoDTO dto) {
        var entity = productoMapper.toEntity(dto);
        var savedEntity = productoService.save(entity);
        var responseDto = productoMapper.toDto(savedEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id,
                                                 @Valid @Validated(ProductoDTO.Update.class) @RequestBody ProductoDTO dto) {
        var entity = productoService.findEntityById(id);
        productoMapper.updateEntityFromDto(dto, entity);
        var updatedEntity = productoService.save(entity);
        var responseDto = productoMapper.toDto(updatedEntity);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoriaId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<List<ProductoDTO>> obtenerPorCategoria(@PathVariable Integer categoriaId) {
        List<ProductoDTO> productos = productoService.findByCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<List<ProductoDTO>> obtenerDisponibles() {
        List<ProductoDTO> productos = productoService.findDisponibles();
        return ResponseEntity.ok(productos);
    }
}
```

### MesaController Refactorizado
```java
package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.MesaService;
import com.example.demo.domain.dto.MesaDTO;
import com.example.demo.domain.mapper.MesaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
@Validated
public class MesaController {

    private final MesaService mesaService;
    private final MesaMapper mesaMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> obtenerTodas() {
        List<MesaDTO> mesas = mesaService.findAll();
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<MesaDTO> obtenerPorId(@PathVariable Integer id) {
        MesaDTO mesa = mesaService.findById(id);
        return ResponseEntity.ok(mesa);
    }

    @GetMapping("/estado/{estadoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> obtenerPorEstado(@PathVariable Integer estadoId) {
        List<MesaDTO> mesas = mesaService.findByEstado(estadoId);
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/ubicacion/{ubicacion}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> obtenerPorUbicacion(@PathVariable String ubicacion) {
        List<MesaDTO> mesas = mesaService.findByUbicacion(ubicacion);
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> obtenerDisponibles() {
        List<MesaDTO> mesas = mesaService.findMesasDisponibles();
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/capacidad/{capacidad}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<List<MesaDTO>> obtenerPorCapacidadMinima(@PathVariable Integer capacidad) {
        List<MesaDTO> mesas = mesaService.findByCapacidadMinima(capacidad);
        return ResponseEntity.ok(mesas);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MesaDTO> crear(@Valid @Validated(MesaDTO.Create.class) @RequestBody MesaDTO dto) {
        var entity = mesaMapper.toEntity(dto);
        var savedEntity = mesaService.save(entity);
        var responseDto = mesaMapper.toDto(savedEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<MesaDTO> actualizar(@PathVariable Integer id,
                                             @Valid @Validated(MesaDTO.Update.class) @RequestBody MesaDTO dto) {
        var entity = mesaService.findEntityById(id);
        mesaMapper.updateEntityFromDto(dto, entity);
        var updatedEntity = mesaService.save(entity);
        var responseDto = mesaMapper.toDto(updatedEntity);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        mesaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

### ComandaController Refactorizado
```java
package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.ComandaService;
import com.example.demo.domain.dto.ComandaDTO;
import com.example.demo.domain.mapper.ComandaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
@RequiredArgsConstructor
@Validated
public class ComandaController {

    private final ComandaService comandaService;
    private final ComandaMapper comandaMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<List<ComandaDTO>> obtenerTodas() {
        List<ComandaDTO> comandas = comandaService.findAll();
        return ResponseEntity.ok(comandas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<ComandaDTO> obtenerPorId(@PathVariable Integer id) {
        ComandaDTO comanda = comandaService.findById(id);
        return ResponseEntity.ok(comanda);
    }

    @GetMapping("/mesa/{mesaId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<List<ComandaDTO>> obtenerPorMesa(@PathVariable Integer mesaId) {
        List<ComandaDTO> comandas = comandaService.findByMesa(mesaId);
        return ResponseEntity.ok(comandas);
    }

    @GetMapping("/estado/{estadoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('CAJERO')")
    public ResponseEntity<List<ComandaDTO>> obtenerPorEstado(@PathVariable Integer estadoId) {
        List<ComandaDTO> comandas = comandaService.findByEstado(estadoId);
        return ResponseEntity.ok(comandas);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO')")
    public ResponseEntity<ComandaDTO> crear(@Valid @Validated(ComandaDTO.Create.class) @RequestBody ComandaDTO dto) {
        var entity = comandaMapper.toEntity(dto);
        var savedEntity = comandaService.save(entity);
        var responseDto = comandaMapper.toDto(savedEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('COCINERO')")
    public ResponseEntity<ComandaDTO> actualizar(@PathVariable Integer id,
                                                @Valid @Validated(ComandaDTO.Update.class) @RequestBody ComandaDTO dto) {
        var entity = comandaService.findEntityById(id);
        comandaMapper.updateEntityFromDto(dto, entity);
        var updatedEntity = comandaService.save(entity);
        var responseDto = comandaMapper.toDto(updatedEntity);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}/estado/{estadoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MESERO') or hasRole('COCINERO')")
    public ResponseEntity<ComandaDTO> cambiarEstado(@PathVariable Integer id, @PathVariable Integer estadoId) {
        ComandaDTO comanda = comandaService.cambiarEstado(id, estadoId);
        return ResponseEntity.ok(comanda);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        comandaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

## 📋 FASE 4 - SERVICES REFACTORIZADOS

### UsuarioService Refactorizado
```java
package com.example.demo.application.service;

import com.example.demo.domain.dto.UsuarioDTO;
import com.example.demo.persistenceLayer.entity.Rol;
import com.example.demo.persistenceLayer.entity.Usuario;
import com.example.demo.persistenceLayer.repository.RolRepository;
import com.example.demo.persistenceLayer.repository.UsuarioRepository;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UsuarioDTO findById(String id) {
        Usuario usuario = findEntityById(id);
        return convertToDto(usuario);
    }

    public Usuario findEntityById(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public UsuarioDTO create(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + dto.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDto(savedUsuario);
    }

    public UsuarioDTO update(String id, UsuarioDTO dto) {
        Usuario usuario = findEntityById(id);

        // Verificar si el email ya existe en otro usuario
        if (!usuario.getEmail().equals(dto.getEmail()) &&
            usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + dto.getRolId()));

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(rol);

        // Solo actualizar contraseña si se proporciona
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return convertToDto(updatedUsuario);
    }

    public void deleteById(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return convertToDto(usuario);
    }

    private UsuarioDTO convertToDto(Usuario usuario) {
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rolId(usuario.getRol() != null ? usuario.getRol().getIdRol() : null)
                .rolNombre(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .build();
    }
}
```

### ProductoService Refactorizado
```java
package com.example.demo.application.service;

import com.example.demo.domain.dto.ProductoDTO;
import com.example.demo.persistenceLayer.entity.Categoria;
import com.example.demo.persistenceLayer.entity.Producto;
import com.example.demo.persistenceLayer.repository.CategoriaRepository;
import com.example.demo.persistenceLayer.repository.ProductoRepository;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO findById(Integer id) {
        Producto producto = findEntityById(id);
        return convertToDto(producto);
    }

    public Producto findEntityById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public ProductoDTO create(ProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        producto.setEstado(dto.getEstado() != null ? dto.getEstado() : true);

        Producto savedProducto = productoRepository.save(producto);
        return convertToDto(savedProducto);
    }

    public ProductoDTO update(Integer id, ProductoDTO dto) {
        Producto producto = findEntityById(id);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setEstado(dto.getEstado());

        Producto updatedProducto = productoRepository.save(producto);
        return convertToDto(updatedProducto);
    }

    public void deleteById(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    public List<ProductoDTO> findByCategoria(Integer categoriaId) {
        return productoRepository.findByCategoriaIdCategoria(categoriaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> findDisponibles() {
        return productoRepository.findByEstado(true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductoDTO convertToDto(Producto producto) {
        return ProductoDTO.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getIdCategoria() : null)
                .categoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null)
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .estado(producto.getEstado())
                .build();
    }
}
```

## 📋 FASE 5 - CONFIGURACIÓN MAPSTRUCT

### pom.xml con MapStruct
```xml
<properties>
    <java.version>21</java.version>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
</properties>

<dependencies>
    <!-- MapStruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${mapstruct.version}</version>
    </dependency>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${mapstruct.version}</version>
        <scope>provided</scope>
    </dependency>

    <!-- Otras dependencias existentes -->
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>21</source>
                <target>21</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>1.18.30</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 📋 FASE 6 - PLAN DE MIGRACIÓN GIT

```bash
#!/bin/bash
# Script de migración completa a Clean Architecture

echo "🚀 Iniciando migración a Clean Architecture..."

# Crear nuevas carpetas
mkdir -p domain/{entity,dto,mapper}
mkdir -p application/service
mkdir -p infrastructure/{persistence/repository,web/controller,config}
mkdir -p shared/{exception,constants,util}

# Mover entidades
echo "📁 Moviendo entidades..."
git mv src/main/java/com/example/demo/persistenceLayer/entity/* domain/entity/

# Mover DTOs consolidados
echo "📄 Moviendo DTOs..."
git mv src/main/java/com/example/demo/businessLayer/dto/* domain/dto/

# Mover services
echo "🔧 Moviendo services..."
git mv src/main/java/com/example/demo/businessLayer/service/* application/service/

# Mover controllers
echo "🌐 Moviendo controllers..."
git mv src/main/java/com/example/demo/presentationLayer/* infrastructure/web/controller/

# Mover repositories
echo "💾 Moviendo repositories..."
git mv src/main/java/com/example/demo/persistenceLayer/repository/* infrastructure/persistence/repository/

# Mover configuración
echo "⚙️ Moviendo configuración..."
git mv src/main/java/com/example/demo/businessLayer/config/* infrastructure/config/
git mv src/main/java/com/example/demo/service/config/* infrastructure/config/

# Mover utilidades compartidas
echo "🛠️ Moviendo utilidades..."
git mv src/main/java/com/example/demo/exception/* shared/exception/
git mv src/main/java/com/example/demo/constants/* shared/constants/
git mv src/main/java/com/example/demo/util/* shared/util/
git mv src/main/java/com/example/demo/securityLayer/* infrastructure/config/

# Eliminar carpetas vacías
echo "🗑️ Eliminando carpetas obsoletas..."
rmdir src/main/java/com/example/demo/businessLayer/dto/request
rmdir src/main/java/com/example/demo/businessLayer/dto/response
rmdir src/main/java/com/example/demo/persistenceLayer/dao
rmdir src/main/java/com/example/demo/businessLayer
rmdir src/main/java/com/example/demo/presentationLayer
rmdir src/main/java/com/example/demo/persistenceLayer
rmdir src/main/java/com/example/demo/service
rmdir src/main/java/com/example/demo/securityLayer

# Actualizar imports en todos los archivos
echo "🔄 Actualizando imports..."
find . -name "*.java" -exec sed -i 's/com\.example\.demo\.businessLayer\.dto/com.example.demo.domain.dto/g' {} \;
find . -name "*.java" -exec sed -i 's/com\.example\.demo\.persistenceLayer\.entity/com.example.demo.domain.entity/g' {} \;
find . -name "*.java" -exec sed -i 's/com\.example\.demo\.businessLayer\.service/com.example.demo.application.service/g' {}