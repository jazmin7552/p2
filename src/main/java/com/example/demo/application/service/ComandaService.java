package com.example.demo.application.service;

import com.example.demo.domain.dto.ComandaDTO;
import com.example.demo.domain.entity.Comanda;
import com.example.demo.domain.entity.Estado;
import com.example.demo.domain.entity.Mesa;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.domain.mapper.ComandaMapper;
import com.example.demo.infrastructure.persistence.repository.ComandaRepository;
import com.example.demo.infrastructure.persistence.repository.EstadoRepository;
import com.example.demo.infrastructure.persistence.repository.MesaRepository;
import com.example.demo.infrastructure.persistence.repository.UsuarioRepository;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final ComandaMapper comandaMapper;
    private final MesaRepository mesaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public List<ComandaDTO> findAll() {
        List<Comanda> comandas = comandaRepository.findAll();
        return comandaMapper.toDtoList(comandas);
    }

    @Transactional(readOnly = true)
    public ComandaDTO findById(Integer id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda no encontrada con ID: " + id));
        return comandaMapper.toDto(comanda);
    }

    public ComandaDTO save(ComandaDTO comandaDTO) {
        Comanda comanda = comandaMapper.toEntity(comandaDTO);

        // Establecer fecha si no existe
        if (comanda.getFecha() == null) {
            comanda.setFecha(LocalDateTime.now());
        }

        // Validar y establecer relaciones
        if (comandaDTO.getMesaId() != null) {
            Mesa mesa = mesaRepository.findById(comandaDTO.getMesaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Mesa no encontrada con ID: " + comandaDTO.getMesaId()));
            comanda.setMesa(mesa);
        }

        if (comandaDTO.getMeseroId() != null) {
            Usuario mesero = usuarioRepository.findById(comandaDTO.getMeseroId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Mesero no encontrado con ID: " + comandaDTO.getMeseroId()));
            comanda.setMesero(mesero);
        }

        if (comandaDTO.getCocineroId() != null) {
            Usuario cocinero = usuarioRepository.findById(comandaDTO.getCocineroId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cocinero no encontrado con ID: " + comandaDTO.getCocineroId()));
            comanda.setCocinero(cocinero);
        }

        if (comandaDTO.getEstadoId() != null) {
            Estado estado = estadoRepository.findById(comandaDTO.getEstadoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Estado no encontrado con ID: " + comandaDTO.getEstadoId()));
            comanda.setEstado(estado);
        } else {
            // Estado por defecto: "Pendiente" (ID 1)
            Estado estadoPendiente = estadoRepository.findById(1)
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Pendiente' no encontrado"));
            comanda.setEstado(estadoPendiente);
        }

        Comanda savedComanda = comandaRepository.save(comanda);
        return comandaMapper.toDto(savedComanda);
    }

    public ComandaDTO update(Integer id, ComandaDTO comandaDTO) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda no encontrada con ID: " + id));

        // Actualizar campos básicos
        comandaMapper.updateEntityFromDto(comandaDTO, comanda);

        // Actualizar relaciones si se proporcionan
        if (comandaDTO.getMesaId() != null) {
            Mesa mesa = mesaRepository.findById(comandaDTO.getMesaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Mesa no encontrada con ID: " + comandaDTO.getMesaId()));
            comanda.setMesa(mesa);
        }

        if (comandaDTO.getMeseroId() != null) {
            Usuario mesero = usuarioRepository.findById(comandaDTO.getMeseroId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Mesero no encontrado con ID: " + comandaDTO.getMeseroId()));
            comanda.setMesero(mesero);
        }

        if (comandaDTO.getCocineroId() != null) {
            Usuario cocinero = usuarioRepository.findById(comandaDTO.getCocineroId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cocinero no encontrado con ID: " + comandaDTO.getCocineroId()));
            comanda.setCocinero(cocinero);
        }

        if (comandaDTO.getEstadoId() != null) {
            Estado estado = estadoRepository.findById(comandaDTO.getEstadoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Estado no encontrado con ID: " + comandaDTO.getEstadoId()));
            comanda.setEstado(estado);
        }

        Comanda updatedComanda = comandaRepository.save(comanda);
        return comandaMapper.toDto(updatedComanda);
    }

    public ComandaDTO cambiarEstado(Integer id, Integer estadoId) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda no encontrada con ID: " + id));

        Estado estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + estadoId));

        comanda.setEstado(estado);
        Comanda updatedComanda = comandaRepository.save(comanda);
        return comandaMapper.toDto(updatedComanda);
    }

    public void deleteById(Integer id) {
        if (!comandaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comanda no encontrada con ID: " + id);
        }
        comandaRepository.deleteById(id);
    }

    // Métodos de búsqueda
    @Transactional(readOnly = true)
    public List<ComandaDTO> findByMesaId(Integer idMesa) {
        List<Comanda> comandas = comandaRepository.findByMesaIdMesa(idMesa);
        return comandaMapper.toDtoList(comandas);
    }

    @Transactional(readOnly = true)
    public List<ComandaDTO> findByMeseroId(String idMesero) {
        List<Comanda> comandas = comandaRepository.findByMeseroIdUsuario(idMesero);
        return comandaMapper.toDtoList(comandas);
    }

    @Transactional(readOnly = true)
    public List<ComandaDTO> findByCocineroId(String idCocinero) {
        List<Comanda> comandas = comandaRepository.findByCocineroIdUsuario(idCocinero);
        return comandaMapper.toDtoList(comandas);
    }

    @Transactional(readOnly = true)
    public List<ComandaDTO> findByEstadoId(Integer idEstado) {
        List<Comanda> comandas = comandaRepository.findByEstadoIdEstado(idEstado);
        return comandaMapper.toDtoList(comandas);
    }

    @Transactional(readOnly = true)
    public List<ComandaDTO> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Comanda> comandas = comandaRepository.findByFechaBetween(fechaInicio, fechaFin);
        return comandaMapper.toDtoList(comandas);
    }

    @Transactional(readOnly = true)
    public List<ComandaDTO> findComandasActivas() {
        // Comandas activas = todas excepto las que están en estado "Pagada" (ID 5)
        List<Comanda> comandas = comandaRepository.findByEstadoIdEstadoNot(5);
        return comandaMapper.toDtoList(comandas);
    }

    @Transactional(readOnly = true)
    public List<ComandaDTO> findComandasHoy() {
        LocalDateTime inicioDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime finDia = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<Comanda> comandas = comandaRepository.findByFechaBetween(inicioDia, finDia);
        return comandaMapper.toDtoList(comandas);
    }
}