package com.example.demo.application.service;

import com.example.demo.domain.dto.MesaDTO;
import com.example.demo.domain.entity.Estado;
import com.example.demo.domain.entity.Mesa;
import com.example.demo.domain.mapper.MesaMapper;
import com.example.demo.infrastructure.persistence.repository.EstadoRepository;
import com.example.demo.infrastructure.persistence.repository.MesaRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para gestión de Mesas
 * LÓGICA DE NEGOCIO:
 * - Mesas con capacidad y ubicación
 * - Estados: DISPONIBLE, OCUPADA, RESERVADA
 * - No se pueden eliminar mesas con comandas activas
 * 
 * USA MAPSTRUCT PARA CONVERSIÓN ENTITY <-> DTO
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MesaService {

    private final MesaRepository mesaRepository;
    private final EstadoRepository estadoRepository;
    private final MesaMapper mesaMapper;

    /**
     * LÓGICA: Obtener todas las mesas
     */
    @Transactional(readOnly = true)
    public List<MesaDTO> findAll() {
        log.info("Obteniendo todas las mesas");
        List<Mesa> mesas = mesaRepository.findAll();
        log.info("Se encontraron {} mesas", mesas.size());
        return mesaMapper.toDtoList(mesas);
    }

    /**
     * LÓGICA: Buscar mesa por ID
     * VALIDACIÓN: ID válido y mesa existe
     */
    @Transactional(readOnly = true)
    public MesaDTO findById(Integer id) {
        log.info("Buscando mesa con ID: {}", id);

        // VALIDACIÓN 1: ID no puede ser nulo o negativo
        if (id == null || id <= 0) {
            log.error("ID inválido: {}", id);
            throw new BadRequestException("El ID de la mesa debe ser un número positivo");
        }

        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mesa no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Mesa no encontrada con ID: " + id);
                });

        log.info("Mesa encontrada: Mesa {} - {}", mesa.getIdMesa(), mesa.getUbicacion());
        return mesaMapper.toDto(mesa);
    }

    /**
     * LÓGICA: Crear nueva mesa
     * VALIDACIONES:
     * - Capacidad entre 1 y 20 personas
     * - Ubicación obligatoria
     * - Estado debe existir
     */
    public MesaDTO create(MesaDTO mesaDTO) {
        log.info("Intentando crear mesa en: {}", mesaDTO.getUbicacion());

        // VALIDACIÓN 1: Capacidad válida
        if (mesaDTO.getCapacidad() == null || mesaDTO.getCapacidad() < 1 || mesaDTO.getCapacidad() > 20) {
            log.error("Capacidad inválida: {}", mesaDTO.getCapacidad());
            throw new BadRequestException("La capacidad debe estar entre 1 y 20 personas");
        }

        // VALIDACIÓN 2: Ubicación obligatoria
        if (mesaDTO.getUbicacion() == null || mesaDTO.getUbicacion().trim().isEmpty()) {
            log.error("Intento de crear mesa sin ubicación");
            throw new BadRequestException("La ubicación de la mesa es obligatoria");
        }

        // VALIDACIÓN 3: Longitud de ubicación
        if (mesaDTO.getUbicacion().length() > 50) {
            log.error("Ubicación demasiado larga: {}", mesaDTO.getUbicacion());
            throw new BadRequestException("La ubicación no puede exceder 50 caracteres");
        }

        // Normalizar ubicación
        String ubicacionNormalizada = mesaDTO.getUbicacion().trim().toUpperCase();

        // VALIDACIÓN 4: Estado debe existir, si no se proporciona usar "DISPONIBLE"
        Estado estado;
        if (mesaDTO.getEstadoId() != null) {
            estado = estadoRepository.findById(mesaDTO.getEstadoId())
                    .orElseThrow(() -> {
                        log.error("Estado no encontrado: {}", mesaDTO.getEstadoId());
                        return new ResourceNotFoundException(
                                "Estado no encontrado con ID: " + mesaDTO.getEstadoId());
                    });
        } else {
            // Estado por defecto: DISPONIBLE
            estado = estadoRepository.findByNombreIgnoreCase("DISPONIBLE")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'DISPONIBLE' no encontrado"));
            log.info("Estado no proporcionado, usando DISPONIBLE por defecto");
        }

        // Crear la mesa usando MAPSTRUCT
        Mesa mesa = mesaMapper.toEntity(mesaDTO);
        mesa.setUbicacion(ubicacionNormalizada);
        mesa.setEstado(estado);

        Mesa mesaSaved = mesaRepository.save(mesa);
        log.info("Mesa creada exitosamente con ID: {} en {}", mesaSaved.getIdMesa(), mesaSaved.getUbicacion());

        return mesaMapper.toDto(mesaSaved);
    }

    /**
     * LÓGICA: Actualizar mesa existente
     * VALIDACIONES:
     * - Mesa debe existir
     * - Validaciones similares a crear
     */
    public MesaDTO update(Integer id, MesaDTO mesaDTO) {
        log.info("Intentando actualizar mesa ID: {}", id);

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para actualización: {}", id);
            throw new BadRequestException("El ID de la mesa debe ser un número positivo");
        }

        // VALIDACIÓN 2: Mesa debe existir
        Mesa mesaExistente = mesaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mesa no encontrada para actualizar con ID: {}", id);
                    return new ResourceNotFoundException("Mesa no encontrada con ID: " + id);
                });

        // VALIDACIÓN 3: Capacidad válida
        if (mesaDTO.getCapacidad() != null) {
            if (mesaDTO.getCapacidad() < 1 || mesaDTO.getCapacidad() > 20) {
                log.error("Capacidad inválida: {}", mesaDTO.getCapacidad());
                throw new BadRequestException("La capacidad debe estar entre 1 y 20 personas");
            }
        }

        // VALIDACIÓN 4: Ubicación válida
        if (mesaDTO.getUbicacion() != null) {
            if (mesaDTO.getUbicacion().trim().isEmpty()) {
                log.error("Intento de actualizar con ubicación vacía");
                throw new BadRequestException("La ubicación de la mesa es obligatoria");
            }
            if (mesaDTO.getUbicacion().length() > 50) {
                log.error("Ubicación demasiado larga: {}", mesaDTO.getUbicacion());
                throw new BadRequestException("La ubicación no puede exceder 50 caracteres");
            }
        }

        // VALIDACIÓN 5: Estado debe existir
        if (mesaDTO.getEstadoId() != null) {
            Estado estado = estadoRepository.findById(mesaDTO.getEstadoId())
                    .orElseThrow(() -> {
                        log.error("Estado no encontrado: {}", mesaDTO.getEstadoId());
                        return new ResourceNotFoundException(
                                "Estado no encontrado con ID: " + mesaDTO.getEstadoId());
                    });
            mesaExistente.setEstado(estado);
        }

        // Actualizar usando MAPSTRUCT
        mesaMapper.updateEntityFromDto(mesaDTO, mesaExistente);

        // Normalizar ubicación si se proporciona
        if (mesaDTO.getUbicacion() != null) {
            mesaExistente.setUbicacion(mesaDTO.getUbicacion().trim().toUpperCase());
        }

        Mesa mesaUpdated = mesaRepository.save(mesaExistente);
        log.info("Mesa actualizada exitosamente: Mesa {} - {}", mesaUpdated.getIdMesa(), mesaUpdated.getUbicacion());

        return mesaMapper.toDto(mesaUpdated);
    }

    /**
     * LÓGICA: Eliminar mesa
     * VALIDACIONES:
     * - Mesa debe existir
     * - No puede tener comandas activas
     */
    public void deleteById(Integer id) {
        log.info("Intentando eliminar mesa con ID: {}", id);

        // VALIDACIÓN 1: ID válido
        if (id == null || id <= 0) {
            log.error("ID inválido para eliminación: {}", id);
            throw new BadRequestException("El ID de la mesa debe ser un número positivo");
        }

        // VALIDACIÓN 2: Mesa debe existir
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mesa no encontrada para eliminar con ID: {}", id);
                    return new ResourceNotFoundException("Mesa no encontrada con ID: " + id);
                });

        // VALIDACIÓN 3: No puede tener comandas asociadas
        if (mesa.getComandas() != null && !mesa.getComandas().isEmpty()) {
            log.error("No se puede eliminar mesa con comandas asociadas. ID: {}", id);
            throw new BadRequestException(
                    "No se puede eliminar la mesa porque tiene " +
                            mesa.getComandas().size() + " comanda(s) asociada(s). " +
                            "Considere cambiar el estado en lugar de eliminarla.");
        }

        mesaRepository.deleteById(id);
        log.info("Mesa eliminada exitosamente: Mesa {} - {}", mesa.getIdMesa(), mesa.getUbicacion());
    }

    /**
     * LÓGICA: Buscar mesas por estado
     */
    @Transactional(readOnly = true)
    public List<MesaDTO> findByEstado(Integer idEstado) {
        log.info("Buscando mesas con estado ID: {}", idEstado);

        // VALIDACIÓN: Estado debe existir
        if (!estadoRepository.existsById(idEstado)) {
            log.error("Estado no encontrado: {}", idEstado);
            throw new ResourceNotFoundException("Estado no encontrado con ID: " + idEstado);
        }

        List<Mesa> mesas = mesaRepository.findByEstadoIdEstado(idEstado);
        log.info("Se encontraron {} mesas con estado {}", mesas.size(), idEstado);

        return mesaMapper.toDtoList(mesas);
    }

    /**
     * LÓGICA: Buscar mesas por ubicación
     */
    @Transactional(readOnly = true)
    public List<MesaDTO> findByUbicacion(String ubicacion) {
        log.info("Buscando mesas en ubicación: {}", ubicacion);

        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            log.error("Ubicación de búsqueda vacía");
            throw new BadRequestException("La ubicación de búsqueda no puede estar vacía");
        }

        List<Mesa> mesas = mesaRepository.findByUbicacion(ubicacion.trim().toUpperCase());
        log.info("Se encontraron {} mesas en '{}'", mesas.size(), ubicacion);

        return mesaMapper.toDtoList(mesas);
    }

    /**
     * LÓGICA: Buscar mesas por capacidad mínima
     */
    @Transactional(readOnly = true)
    public List<MesaDTO> findByCapacidadMinima(Integer capacidad) {
        log.info("Buscando mesas con capacidad >= {}", capacidad);

        if (capacidad == null || capacidad < 1) {
            log.error("Capacidad inválida: {}", capacidad);
            throw new BadRequestException("La capacidad debe ser mayor a 0");
        }

        List<Mesa> mesas = mesaRepository.findByCapacidadGreaterThanEqual(capacidad);
        log.info("Se encontraron {} mesas con capacidad >= {}", mesas.size(), capacidad);

        return mesaMapper.toDtoList(mesas);
    }

    /**
     * LÓGICA: Obtener solo mesas disponibles
     */
    @Transactional(readOnly = true)
    public List<MesaDTO> findMesasDisponibles() {
        log.info("Obteniendo mesas disponibles");

        Estado estadoDisponible = estadoRepository.findByNombreIgnoreCase("DISPONIBLE")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'DISPONIBLE' no encontrado"));

        return findByEstado(estadoDisponible.getIdEstado());
    }

    /**
     * LÓGICA: Cambiar estado de una mesa
     */
    public MesaDTO cambiarEstado(Integer idMesa, Integer idEstado) {
        log.info("Cambiando estado de mesa {} a estado {}", idMesa, idEstado);

        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + idMesa));

        Estado estado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + idEstado));

        mesa.setEstado(estado);
        Mesa mesaUpdated = mesaRepository.save(mesa);

        log.info("Estado de mesa {} cambiado a {}", idMesa, estado.getNombre());
        return mesaMapper.toDto(mesaUpdated);
    }

    /**
     * LÓGICA: Verificar disponibilidad de mesa
     */
    @Transactional(readOnly = true)
    public boolean estaDisponible(Integer idMesa) {
        log.info("Verificando disponibilidad de mesa {}", idMesa);

        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + idMesa));

        boolean disponible = mesa.getEstado() != null &&
                mesa.getEstado().getNombre().equalsIgnoreCase("DISPONIBLE");

        log.info("Mesa {} está {}", idMesa, disponible ? "DISPONIBLE" : "OCUPADA/RESERVADA");
        return disponible;
    }
}