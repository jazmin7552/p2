package mesa.application.service;

import com.example.demo.application.service.MesaService;
import com.example.demo.domain.entity.Estado;
import com.example.demo.domain.entity.Mesa;
import com.example.demo.domain.mapper.MesaMapper;
import com.example.demo.infrastructure.persistence.repository.MesaRepository;
import com.example.demo.infrastructure.persistence.repository.EstadoRepository;
import com.example.demo.domain.dto.MesaDTO;
import com.example.demo.domain.entity.Comanda;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MesaService")
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private MesaMapper mesaMapper;

    @InjectMocks
    private MesaService mesaService;

    private Mesa mesa;
    private MesaDTO requestDto;

    @BeforeEach
    void setUp() {
        Estado estado = new Estado();
        estado.setIdEstado(1);
        estado.setNombre("DISPONIBLE");

        mesa = new Mesa();
        mesa.setIdMesa(1);
        mesa.setCapacidad(4);
        mesa.setUbicacion("TERRAZA");
        mesa.setEstado(estado);

        requestDto = MesaDTO.builder()
                .capacidad(4)
                .ubicacion("TERRAZA")
                .build();
    }

    @Test
    @DisplayName("Crear mesa exitosamente")
    void create_Exitoso() {
        // Given
        Estado estado = new Estado();
        estado.setIdEstado(1);
        estado.setNombre("DISPONIBLE");

        when(estadoRepository.findByNombreIgnoreCase("DISPONIBLE")).thenReturn(Optional.of(estado));
        when(mesaMapper.toEntity(requestDto)).thenReturn(mesa);
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);
        when(mesaMapper.toDto(mesa)).thenReturn(requestDto);

        // When
        MesaDTO response = mesaService.create(requestDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCapacidad()).isEqualTo(4);
        assertThat(response.getUbicacion()).isEqualTo("TERRAZA");
        verify(mesaRepository, times(1)).save(any(Mesa.class));
    }

    @Test
    @DisplayName("Crear mesa con capacidad inválida debe lanzar excepción")
    void create_CapacidadInvalida_DeberiaLanzarExcepcion() {
        // Given
        requestDto.setCapacidad(0);

        // When & Then
        assertThatThrownBy(() -> mesaService.create(requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("La capacidad debe estar entre 1 y 20 personas");
        verify(mesaRepository, never()).save(any(Mesa.class));
    }

    @Test
    @DisplayName("Obtener mesa por ID exitosamente")
    void findById_Exitoso() {
        // Given
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        when(mesaMapper.toDto(mesa)).thenReturn(requestDto);

        // When
        MesaDTO response = mesaService.findById(1);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getIdMesa()).isEqualTo(1);
        verify(mesaRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Obtener mesa inexistente debe lanzar excepción")
    void findById_NoExiste_DeberiaLanzarExcepcion() {
        // Given
        when(mesaRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> mesaService.findById(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Mesa no encontrada");
    }

    @Test
    @DisplayName("Listar todas las mesas")
    void findAll_DeberiaRetornarLista() {
        // Given
        Estado estadoOcupada = new Estado();
        estadoOcupada.setIdEstado(2);
        estadoOcupada.setNombre("OCUPADA");

        Mesa mesa2 = new Mesa();
        mesa2.setIdMesa(2);
        mesa2.setCapacidad(6);
        mesa2.setUbicacion("SALON");
        mesa2.setEstado(estadoOcupada);

        when(mesaRepository.findAll()).thenReturn(Arrays.asList(mesa, mesa2));
        when(mesaMapper.toDtoList(Arrays.asList(mesa, mesa2))).thenReturn(Arrays.asList(requestDto, requestDto));

        // When
        List<MesaDTO> response = mesaService.findAll();

        // Then
        assertThat(response).hasSize(2);
    }

    @Test
    @DisplayName("Actualizar mesa exitosamente")
    void update_Exitoso() {
        // Given
        MesaDTO updateDto = MesaDTO.builder()
                .capacidad(6)
                .ubicacion("SALON")
                .build();

        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);
        when(mesaMapper.toDto(mesa)).thenReturn(updateDto);

        // When
        MesaDTO response = mesaService.update(1, updateDto);

        // Then
        assertThat(response).isNotNull();
        verify(mesaRepository, times(1)).save(any(Mesa.class));
    }

    @Test
    @DisplayName("Cambiar estado de mesa exitosamente")
    void cambiarEstado_Exitoso() {
        // Given
        Estado estadoOcupada = new Estado();
        estadoOcupada.setIdEstado(2);
        estadoOcupada.setNombre("OCUPADA");

        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estadoOcupada));
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);
        when(mesaMapper.toDto(mesa)).thenReturn(requestDto);

        // When
        MesaDTO response = mesaService.cambiarEstado(1, 2);

        // Then
        assertThat(response).isNotNull();
        verify(mesaRepository, times(1)).save(any(Mesa.class));
    }

    @Test
    @DisplayName("Eliminar mesa exitosamente")
    void deleteById_Exitoso() {
        // Given
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        doNothing().when(mesaRepository).deleteById(1);

        // When
        mesaService.deleteById(1);

        // Then
        verify(mesaRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("No se puede eliminar mesa con comandas asociadas")
    void deleteById_ConComandas_DeberiaLanzarExcepcion() {
        // Given
        mesa.setComandas(Arrays.asList(new Comanda())); // Simular comanda asociada
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));

        // When & Then
        assertThatThrownBy(() -> mesaService.deleteById(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No se puede eliminar la mesa porque tiene");
        verify(mesaRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Obtener mesas por estado")
    void findByEstado_DeberiaRetornarLista() {
        // Given
        when(estadoRepository.existsById(1)).thenReturn(true);
        when(mesaRepository.findByEstadoIdEstado(1)).thenReturn(Arrays.asList(mesa));
        when(mesaMapper.toDtoList(Arrays.asList(mesa))).thenReturn(Arrays.asList(requestDto));

        // When
        List<MesaDTO> response = mesaService.findByEstado(1);

        // Then
        assertThat(response).hasSize(1);
    }

    @Test
    @DisplayName("Obtener mesas disponibles")
    void findMesasDisponibles_DeberiaRetornarSoloDisponibles() {
        // Given
        Estado estadoDisponible = new Estado();
        estadoDisponible.setIdEstado(1);
        estadoDisponible.setNombre("DISPONIBLE");

        when(estadoRepository.findByNombreIgnoreCase("DISPONIBLE")).thenReturn(Optional.of(estadoDisponible));
        when(mesaRepository.findByEstadoIdEstado(1)).thenReturn(Arrays.asList(mesa));
        when(mesaMapper.toDtoList(Arrays.asList(mesa))).thenReturn(Arrays.asList(requestDto));

        // When
        List<MesaDTO> response = mesaService.findMesasDisponibles();

        // Then
        assertThat(response).hasSize(1);
    }
}
