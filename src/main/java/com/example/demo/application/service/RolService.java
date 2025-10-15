package com.example.demo.application.service;

import com.example.demo.domain.dto.RolDTO;
import com.example.demo.domain.entity.Rol;
import com.example.demo.domain.mapper.RolMapper;
import com.example.demo.infrastructure.persistence.repository.RolRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para gestión de Roles (ADMIN, MESERO, COCINERO, etc.)
 * LÓGICA DE NEGOCIO:
 * - Roles del sistema para control de acceso
 * - No se pueden eliminar roles con usuarios asociados
 * - Nombres normalizados a MAYÚSCULAS
 * 
 * USA MAPSTRUCT PARA CONVERSIÓN ENTITY <-> DTO
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    /**
     * LÓGICA: Obtener todos los roles
     */
    @Transactional(readOnly = true)
    public List<RolDTO> findAll() {
        log.info("Obteniendo todos los roles");
        List<Rol> roles = rolRepository.findAll();
        log.info("Se encontraron {} roles", roles.size());
        return rolMapper.toDtoList(roles);
    }

    /**
     * LÓGICA: Buscar rol por ID
     * VALIDACIÓN: ID válido y rol existe
     */
    @Transactional(readOnly = true)
    public RolDTO findById(Integer id) {
        log.info("Buscando rol con ID: {}", id);

        // VALIDACIÓN 1: ID no puede ser nulo o negativo
        if (id == null || id <= 0) {
            log.error("ID inválido: {}", id);
            throw new BadRequestException("El ID del rol debe ser un número positivo");
        }

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Rol no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Rol no encontrado con ID: " + id);
                });

        log.info("Rol encontrado: {}", rol.getNombre());
        return rolMapper.toDto(rol);
    }

    /**
     * LÓGICA: Crear nuevo rol
     * VALIDACIONES:
     * - Nombre obligatorio y único
     * - Longitud máxima 20 caracteres
     * - Solo letras, números y guiones bajos
     */
    public RolDTO create(RolDTO rolDTO) {
        log.info("Intentando crear rol: {}", rolDTO.getNombre());

        // VALIDACIÓN 1: Nombre no puede ser nulo o vacío
        if (rolDTO.getNombre() == null || rolDTO.getNombre().trim().isEmpty()) {
            log.error("Intento de crear rol con nombre vacío");
            throw new BadRequestException("El nombre del rol es obligatorio");
        }

        // VALIDACIÓN 2: Longitud máxima
        if (rolDTO.getNombre().length() > 20) {
            log.error("Nombre demasiado largo: {}", rolDTO.getNombre());
            throw new BadRequestException("El nombre del rol no puede exceder 20 caracteres");
        }

        // VALIDACIÓN 3: Formato válido (letras, números, guiones bajos)
        String nombreNormalizado = rolDTO.getNombre().trim().toUpperCase().replace(" ", "_");
        if (!nombreNormalizado.matches("^[A-Z0-9_]+$")) {
            log.error("Formato de nombre inválido: {}", rolDTO.getNombre());
            throw new BadRequestException(
                    "El nombre del rol solo puede contener letras, números y guiones bajos");
        }

        // VALIDACIÓN 4: No puede existir otro rol con el mismo nombre
        if (rolRepository.existsByNombre(nombreNormalizado)) {
            log.error("Ya existe un rol con el nombre: {}", rolDTO.getNombre());
            throw new BadRequestException("Ya existe un rol con el nombre: " + nombreNormalizado);
        }

        // Crear el rol usando MAPSTRUCT
        Rol rol = rolMapper.toEntity(rolDTO);
        rol.setNombre(nombreNormalizado);

        Rol rolSaved = rolRepository.save(rol);
        log.info("Rol creado exitosamente con ID: {}", rolSaved.getIdRol());

        return rolMapper.toDto(rolSaved);
    }

    /**
     * LÓGICA: Actualizar rol existente
     * VALIDACIONES:
     * - Rol debe existir
     * - Validaciones similares a crear
     */
    public RolDTO update(Integer id, RolDTO rolDTO) {
        log.info("Intentando actualizar rol ID: {}", id);

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para actualización: {}", id);
            throw new BadRequestException("El ID del rol debe ser un número positivo");
        }

        // VALIDACIÓN 2: Rol debe existir
        Rol rolExistente = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Rol no encontrado para actualizar con ID: {}", id);
                    return new ResourceNotFoundException("Rol no encontrado con ID: " + id);
                });

        // VALIDACIÓN 3: Nombre obligatorio
        if (rolDTO.getNombre() == null || rolDTO.getNombre().trim().isEmpty()) {
            log.error("Intento de actualizar con nombre vacío");
            throw new BadRequestException("El nombre del rol es obligatorio");
        }

        // VALIDACIÓN 4: Longitud máxima
        if (rolDTO.getNombre().length() > 20) {
            log.error("Nombre demasiado largo: {}", rolDTO.getNombre());
            throw new BadRequestException("El nombre del rol no puede exceder 20 caracteres");
        }

        // VALIDACIÓN 5: Formato válido
        String nombreNormalizado = rolDTO.getNombre().trim().toUpperCase().replace(" ", "_");
        if (!nombreNormalizado.matches("^[A-Z0-9_]+$")) {
            log.error("Formato de nombre inválido: {}", rolDTO.getNombre());
            throw new BadRequestException(
                    "El nombre del rol solo puede contener letras, números y guiones bajos");
        }

        // VALIDACIÓN 6: Nombre no duplicado
        if (!rolExistente.getNombre().equalsIgnoreCase(nombreNormalizado)) {
            if (rolRepository.existsByNombre(nombreNormalizado)) {
                log.error("Ya existe otro rol con el nombre: {}", rolDTO.getNombre());
                throw new BadRequestException("Ya existe un rol con el nombre: " + nombreNormalizado);
            }
        }

        // Actualizar usando MAPSTRUCT
        rolMapper.updateEntityFromDto(rolDTO, rolExistente);
        rolExistente.setNombre(nombreNormalizado);

        Rol rolUpdated = rolRepository.save(rolExistente);
        log.info("Rol actualizado exitosamente: {}", rolUpdated.getNombre());

        return rolMapper.toDto(rolUpdated);
    }

    /**
     * LÓGICA: Eliminar rol
     * VALIDACIONES:
     * - Rol debe existir
     * - No puede tener usuarios asociados
     */
    public void deleteById(Integer id) {
        log.info("Intentando eliminar rol con ID: {}", id);

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para eliminación: {}", id);
            throw new BadRequestException("El ID del rol debe ser un número positivo");
        }

        // VALIDACIÓN 2: Rol debe existir
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Rol no encontrado para eliminar con ID: {}", id);
                    return new ResourceNotFoundException("Rol no encontrado con ID: " + id);
                });

        // VALIDACIÓN 3: No puede tener usuarios asociados
        if (rol.getUsuarios() != null && !rol.getUsuarios().isEmpty()) {
            log.error("No se puede eliminar rol con usuarios asociados. ID: {}", id);
            throw new BadRequestException(
                    "No se puede eliminar el rol porque tiene " +
                            rol.getUsuarios().size() + " usuario(s) asociado(s)");
        }

        rolRepository.deleteById(id);
        log.info("Rol eliminado exitosamente: {}", rol.getNombre());
    }

    /**
     * LÓGICA: Buscar por nombre
     */
    @Transactional(readOnly = true)
    public RolDTO findByNombre(String nombre) {
        log.info("Buscando rol por nombre: {}", nombre);

        if (nombre == null || nombre.trim().isEmpty()) {
            log.error("Intento de buscar con nombre vacío");
            throw new BadRequestException("El nombre de búsqueda no puede estar vacío");
        }

        Rol rol = rolRepository.findByNombre(nombre.trim().toUpperCase())
                .orElseThrow(() -> {
                    log.error("Rol no encontrado con nombre: {}", nombre);
                    return new ResourceNotFoundException("Rol no encontrado: " + nombre);
                });

        log.info("Rol encontrado con ID: {}", rol.getIdRol());
        return rolMapper.toDto(rol);
    }

    /**
     * LÓGICA: Verificar si existe por nombre
     */
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        log.info("Verificando existencia de rol: {}", nombre);

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        boolean exists = rolRepository.existsByNombre(nombre.trim().toUpperCase());
        log.info("Rol '{}' existe: {}", nombre, exists);
        return exists;
    }
}