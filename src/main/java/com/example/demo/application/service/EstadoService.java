package com.example.demo.application.service;

import com.example.demo.domain.dto.EstadoDTO;
import com.example.demo.domain.entity.Estado;
import com.example.demo.domain.mapper.EstadoMapper;
import com.example.demo.infrastructure.persistence.repository.EstadoRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    // ---------- MÉTODOS PRINCIPALES ---------- //

    @Transactional(readOnly = true)
    public List<EstadoDTO> findAll() {
        log.info("Obteniendo todos los estados");
        return estadoMapper.toDtoList(estadoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public EstadoDTO findById(Integer id) {
        validarId(id);
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + id));
        return estadoMapper.toDto(estado);
    }

    @Transactional(readOnly = true)
    public EstadoDTO findByNombre(String nombre) {
        String nombreValido = validarNombreNoVacio(nombre);
        Estado estado = estadoRepository.findByNombreIgnoreCase(nombreValido)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + nombreValido));
        return estadoMapper.toDto(estado);
    }

    public EstadoDTO save(EstadoDTO estadoDTO) {
        String nombreNormalizado = validarYNormalizarNombre(estadoDTO.getNombre());

        if (estadoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new BadRequestException("Ya existe un estado con el nombre: " + nombreNormalizado);
        }

        Estado estado = estadoMapper.toEntity(estadoDTO);
        estado.setNombre(nombreNormalizado);

        return estadoMapper.toDto(estadoRepository.save(estado));
    }

    public EstadoDTO update(Integer id, EstadoDTO estadoDTO) {
        validarId(id);
        Estado estadoExistente = obtenerEstado(id);

        String nombreNormalizado = validarYNormalizarNombre(estadoDTO.getNombre());
        if (!estadoExistente.getNombre().equalsIgnoreCase(nombreNormalizado) &&
                estadoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new BadRequestException("Ya existe un estado con el nombre: " + nombreNormalizado);
        }

        estadoMapper.updateEntityFromDto(estadoDTO, estadoExistente);
        estadoExistente.setNombre(nombreNormalizado);

        return estadoMapper.toDto(estadoRepository.save(estadoExistente));
    }

    public void deleteById(Integer id) {
        validarId(id);
        Estado estado = obtenerEstado(id);

        if (tieneAsociaciones(estado)) {
            throw new BadRequestException("No se puede eliminar el estado porque tiene entidades asociadas");
        }

        estadoRepository.deleteById(id);
        log.info("Estado eliminado exitosamente: {}", estado.getNombre());
    }

    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            return false;
        return estadoRepository.existsByNombreIgnoreCase(nombre.trim().toUpperCase());
    }

    @Transactional(readOnly = true)
    public List<EstadoDTO> getEstadosParaMesas() {
        List<String> nombres = List.of("DISPONIBLE", "OCUPADA", "RESERVADA");
        return estadoMapper.toDtoList(estadoRepository.findByNombreIn(nombres));
    }

    @Transactional(readOnly = true)
    public List<EstadoDTO> getEstadosParaComandas() {
        List<String> nombres = List.of("PENDIENTE", "EN_PROCESO", "COMPLETADA", "CANCELADA");
        return estadoMapper.toDtoList(estadoRepository.findByNombreIn(nombres));
    }

    // ---------- MÉTODOS PRIVADOS DE VALIDACIÓN ---------- //

    private void validarId(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El ID del estado debe ser un número positivo");
        }
    }

    private Estado obtenerEstado(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + id));
    }

    private String validarNombreNoVacio(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BadRequestException("El nombre no puede estar vacío");
        }
        return nombre.trim();
    }

    private String validarYNormalizarNombre(String nombre) {
        validarNombreNoVacio(nombre);

        if (nombre.length() > 10) {
            throw new BadRequestException("El nombre del estado no puede exceder 10 caracteres");
        }

        String normalizado = nombre.trim().toUpperCase().replace(" ", "_");

        if (!normalizado.matches("^[A-Z_]+$")) {
            throw new BadRequestException("El nombre solo puede contener letras mayúsculas y guiones bajos");
        }

        return normalizado;
    }

    private boolean tieneAsociaciones(Estado estado) {
        return (estado.getMesas() != null && !estado.getMesas().isEmpty())
                || (estado.getComandas() != null && !estado.getComandas().isEmpty());
    }
}
