package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
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

    private LocalDateTime fecha;

    private Integer mesaId;
    private String mesaUbicacion;

    private String meseroId;
    private String meseroNombre;

    private String cocineroId;
    private String cocineroNombre;

    private Integer estadoId;
    private String estadoNombre;

    private List<com.example.demo.domain.dto.DetalleComandaDTO> detalles;

    /**
     * <-- ESTE CAMPO debe ser BigDecimal para coincidir con el mapper que suma
     * subtotales.
     */
    private BigDecimal total;
}
