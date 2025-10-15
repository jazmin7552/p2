package com.example.demo.application.service;

import com.example.demo.infrastructure.web.dto.EstadisticasDTO;
import com.example.demo.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final MesaRepository mesaRepository;
    private final ProductoRepository productoRepository;
    private final ComandaRepository comandaRepository;

    public EstadisticasDTO getEstadisticasGenerales() {
        return EstadisticasDTO.builder()
                .totalComandas(comandaRepository.count())
                .totalProductos(productoRepository.count())
                .totalMesas(mesaRepository.count())
                .totalUsuarios(usuarioRepository.count())
                .build();
    }

    public EstadisticasDTO getEstadisticasHoy() {
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        long comandasHoy = comandaRepository.findByFechaBetween(inicioDia, finDia).size();

        return EstadisticasDTO.builder()
                .totalComandas(comandasHoy)
                .build();
    }
}