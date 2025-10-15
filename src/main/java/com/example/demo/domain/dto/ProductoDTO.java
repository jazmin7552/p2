package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idProducto;

    @NotBlank(message = "El nombre es obligatorio", groups = { Create.class, Update.class })
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotNull(message = "La categoría es obligatoria", groups = { Create.class, Update.class })
    private Integer categoriaId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String categoriaNombre;

    @NotNull(message = "El precio es obligatorio", groups = { Create.class, Update.class })
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private Integer stock;

    @NotNull(message = "El estado es obligatorio", groups = { Create.class, Update.class })
    private Boolean estado;

    private String descripcion;

    public interface Create {
    }

    public interface Update {
    }
}
