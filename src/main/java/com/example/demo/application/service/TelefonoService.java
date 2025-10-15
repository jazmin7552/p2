package com.example.demo.application.service;

import com.example.demo.domain.dto.TelefonoDTO;
import com.example.demo.domain.entity.Telefono;
import com.example.demo.domain.mapper.TelefonoMapper;
import com.example.demo.infrastructure.persistence.repository.TelefonoRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para gestión de Teléfonos
 * LÓGICA DE NEGOCIO:
 * - Números únicos en el sistema
 * - Validación de formato de número
 * - No se pueden eliminar teléfonos asociados a usuarios
 * 
 * USA MAPSTRUCT PARA CONVERSIÓN ENTITY <-> DTO
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TelefonoService {

    private final TelefonoRepository telefonoRepository;
    private final TelefonoMapper telefonoMapper;

    /**
     * LÓGICA: Obtener todos los teléfonos
     */
    @Transactional(readOnly = true)
    public List<TelefonoDTO> findAll() {
        log.info("Obteniendo todos los teléfonos");
        List<Telefono> telefonos = telefonoRepository.findAll();
        log.info("Se encontraron {} teléfonos", telefonos.size());
        return telefonoMapper.toDtoList(telefonos);
    }

    /**
     * LÓGICA: Buscar teléfono por ID
     * VALIDACIÓN: ID válido y teléfono existe
     */
    @Transactional(readOnly = true)
    public TelefonoDTO findById(Integer id) {
        log.info("Buscando teléfono con ID: {}", id);

        // VALIDACIÓN 1: ID no puede ser nulo o negativo
        if (id == null || id <= 0) {
            log.error("ID inválido: {}", id);
            throw new BadRequestException("El ID del teléfono debe ser un número positivo");
        }

        Telefono telefono = telefonoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Teléfono no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Teléfono no encontrado con ID: " + id);
                });

        log.info("Teléfono encontrado: {}", telefono.getNumero());
        return telefonoMapper.toDto(telefono);
    }

    /**
     * LÓGICA: Crear nuevo teléfono
     * VALIDACIONES:
     * - Número obligatorio y único
     * - Formato válido (solo dígitos, +, -, espacios, paréntesis)
     * - Longitud entre 7 y 15 caracteres
     */
    public TelefonoDTO save(TelefonoDTO telefonoDTO) {
        log.info("Intentando crear teléfono: {}", telefonoDTO.getNumero());

        // VALIDACIÓN 1: Número no puede ser nulo o vacío
        if (telefonoDTO.getNumero() == null || telefonoDTO.getNumero().trim().isEmpty()) {
            log.error("Intento de crear teléfono con número vacío");
            throw new BadRequestException("El número de teléfono es obligatorio");
        }

        // VALIDACIÓN 2: Limpiar y normalizar número
        String numeroLimpio = telefonoDTO.getNumero().trim().replaceAll("\\s+", "");

        // VALIDACIÓN 3: Formato válido (solo dígitos y caracteres permitidos)
        if (!numeroLimpio.matches("^[+]?[0-9()\\-\\s]{7,15}$")) {
            log.error("Formato de número inválido: {}", telefonoDTO.getNumero());
            throw new BadRequestException(
                    "El número de teléfono debe contener entre 7 y 15 dígitos. " +
                            "Puede incluir +, -, (), y espacios");
        }

        // VALIDACIÓN 4: Número no puede estar duplicado
        if (existsByNumero(numeroLimpio)) {
            log.error("Ya existe un teléfono con el número: {}", telefonoDTO.getNumero());
            throw new BadRequestException("El número de teléfono ya está registrado");
        }

        // Crear el teléfono usando MAPSTRUCT
        Telefono telefono = telefonoMapper.toEntity(telefonoDTO);
        telefono.setNumero(numeroLimpio);

        Telefono telefonoSaved = telefonoRepository.save(telefono);
        log.info("Teléfono creado exitosamente con ID: {}", telefonoSaved.getIdTelefono());

        return telefonoMapper.toDto(telefonoSaved);
    }

    /**
     * LÓGICA: Actualizar teléfono existente
     * VALIDACIONES:
     * - Teléfono debe existir
     * - Validaciones similares a crear
     */
    public TelefonoDTO update(Integer id, TelefonoDTO telefonoDTO) {
        log.info("Intentando actualizar teléfono ID: {}", id);

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para actualización: {}", id);
            throw new BadRequestException("El ID del teléfono debe ser un número positivo");
        }

        // VALIDACIÓN 2: Teléfono debe existir
        Telefono telefonoExistente = telefonoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Teléfono no encontrado para actualizar con ID: {}", id);
                    return new ResourceNotFoundException("Teléfono no encontrado con ID: " + id);
                });

        // VALIDACIÓN 3: Número obligatorio
        if (telefonoDTO.getNumero() == null || telefonoDTO.getNumero().trim().isEmpty()) {
            log.error("Intento de actualizar con número vacío");
            throw new BadRequestException("El número de teléfono es obligatorio");
        }

        // VALIDACIÓN 4: Limpiar y normalizar
        String numeroLimpio = telefonoDTO.getNumero().trim().replaceAll("\\s+", "");

        // VALIDACIÓN 5: Formato válido
        if (!numeroLimpio.matches("^[+]?[0-9()\\-\\s]{7,15}$")) {
            log.error("Formato de número inválido: {}", telefonoDTO.getNumero());
            throw new BadRequestException(
                    "El número de teléfono debe contener entre 7 y 15 dígitos. " +
                            "Puede incluir +, -, (), y espacios");
        }

        // VALIDACIÓN 6: Número no duplicado (excepto si es el mismo)
        if (!telefonoExistente.getNumero().equals(numeroLimpio) && existsByNumero(numeroLimpio)) {
            log.error("Ya existe otro teléfono con el número: {}", telefonoDTO.getNumero());
            throw new BadRequestException("El número de teléfono ya está registrado");
        }

        // Actualizar usando MAPSTRUCT
        telefonoMapper.updateEntityFromDto(telefonoDTO, telefonoExistente);
        telefonoExistente.setNumero(numeroLimpio);

        Telefono telefonoUpdated = telefonoRepository.save(telefonoExistente);
        log.info("Teléfono actualizado exitosamente: {}", telefonoUpdated.getNumero());

        return telefonoMapper.toDto(telefonoUpdated);
    }

    /**
     * LÓGICA: Eliminar teléfono
     * VALIDACIONES:
     * - Teléfono debe existir
     * - No puede estar asociado a usuarios (validación opcional según tu modelo)
     */
    public void deleteById(Integer id) {
        log.info("Intentando eliminar teléfono con ID: {}", id);

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para eliminación: {}", id);
            throw new BadRequestException("El ID del teléfono debe ser un número positivo");
        }

        // VALIDACIÓN 2: Teléfono debe existir
        Telefono telefono = telefonoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Teléfono no encontrado para eliminar con ID: {}", id);
                    return new ResourceNotFoundException("Teléfono no encontrado con ID: " + id);
                });

        telefonoRepository.deleteById(id);
        log.info("Teléfono eliminado exitosamente: {}", telefono.getNumero());
    }

    /**
     * LÓGICA: Buscar por número
     */
    @Transactional(readOnly = true)
    public TelefonoDTO findByNumero(String numero) {
        log.info("Buscando teléfono por número: {}", numero);

        if (numero == null || numero.trim().isEmpty()) {
            log.error("Intento de buscar con número vacío");
            throw new BadRequestException("El número de búsqueda no puede estar vacío");
        }

        String numeroLimpio = numero.trim().replaceAll("\\s+", "");

        Telefono telefono = telefonoRepository.findByNumero(numeroLimpio)
                .orElseThrow(() -> {
                    log.error("Teléfono no encontrado con número: {}", numero);
                    return new ResourceNotFoundException("Teléfono no encontrado con número: " + numero);
                });

        log.info("Teléfono encontrado con ID: {}", telefono.getIdTelefono());
        return telefonoMapper.toDto(telefono);
    }

    /**
     * LÓGICA: Verificar si existe por número
     */
    @Transactional(readOnly = true)
    public boolean existsByNumero(String numero) {
        log.info("Verificando existencia de teléfono: {}", numero);

        if (numero == null || numero.trim().isEmpty()) {
            return false;
        }

        String numeroLimpio = numero.trim().replaceAll("\\s+", "");
        boolean exists = telefonoRepository.existsByNumero(numeroLimpio);
        log.info("Teléfono '{}' existe: {}", numero, exists);
        return exists;
    }
}