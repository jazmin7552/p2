package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.DashboardService;
import com.example.demo.infrastructure.web.dto.EstadisticasDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para estadísticas y reportes del restaurante")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/estadisticas")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener estadísticas generales", description = "Devuelve estadísticas generales del restaurante")
    public ResponseEntity<EstadisticasDTO> getEstadisticasGenerales() {
        // TODO: Implementar lógica para obtener estadísticas generales
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ventas-hoy")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    @Operation(summary = "Obtener estadísticas de ventas del día", description = "Devuelve estadísticas de ventas del día actual")
    public ResponseEntity<EstadisticasDTO> getEstadisticasHoy() {
        // TODO: Implementar lógica para obtener estadísticas de ventas del día
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mesas-ocupadas")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Obtener estado actual de mesas", description = "Devuelve información sobre mesas ocupadas/disponibles")
    public ResponseEntity<EstadisticasDTO> getEstadoMesas() {
        // TODO: Implementar lógica para obtener estado de mesas
        return ResponseEntity.ok().build();
    }
}
