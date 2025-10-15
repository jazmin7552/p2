package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class DetalleComandaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idDetalleComanda;

    @NotNull(message = "La comanda es obligatoria", groups = Create.class)
    private Integer comandaId;

    @NotNull(message = "El producto es obligatorio")
    private Integer productoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productoNombre;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal precioUnitario;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal subtotal;

    public interface Create {
    }

    public interface Update {
    }
}
