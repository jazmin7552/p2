package com.example.demo.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDTO {

    private Long totalComandas;
    private Long totalProductos;
    private Long totalMesas;
    private Long totalUsuarios;
    private BigDecimal ventasTotales;
    private BigDecimal promedioVentas;
}