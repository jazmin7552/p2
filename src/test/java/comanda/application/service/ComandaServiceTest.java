package comanda.application.service;

import com.example.demo.application.service.ComandaService;
import com.example.demo.domain.entity.Categoria;
import com.example.demo.domain.entity.Comanda;
import com.example.demo.domain.entity.DetalleComanda;
import com.example.demo.domain.entity.Estado;
import com.example.demo.domain.entity.Mesa;
import com.example.demo.domain.entity.Producto;
import com.example.demo.domain.entity.Rol;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.domain.dto.ComandaDTO;
import com.example.demo.domain.dto.DetalleComandaDTO;
import com.example.demo.domain.mapper.ComandaMapper;
import com.example.demo.infrastructure.persistence.repository.ComandaRepository;
import com.example.demo.infrastructure.persistence.repository.DetalleComandaRepository;
import com.example.demo.infrastructure.persistence.repository.EstadoRepository;
import com.example.demo.infrastructure.persistence.repository.MesaRepository;
import com.example.demo.infrastructure.persistence.repository.ProductoRepository;
import com.example.demo.infrastructure.persistence.repository.UsuarioRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.InsufficientStockException;
import com.example.demo.shared.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ComandaService")
class ComandaServiceTest {

    @Mock
    private ComandaRepository comandaRepository;

    @Mock
    private DetalleComandaRepository detalleComandaRepository;

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private ComandaMapper comandaMapper;

    @InjectMocks
    private ComandaService comandaService;

    private Mesa mesa;
    private Usuario usuario;
    private Producto producto;
    private Categoria categoria;
    private Estado estado;
    private Comanda comanda;
    private ComandaDTO requestDto;

    @BeforeEach
    void setUp() {
        // Setup Estado
        estado = new Estado();
        estado.setIdEstado(1);
        estado.setNombre("Pendiente");

        // Setup Mesa
        mesa = new Mesa();
        mesa.setIdMesa(1);
        mesa.setCapacidad(4);
        mesa.setUbicacion("Terraza");
        mesa.setEstado(estado);

        // Setup Categoria
        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNombre("Bebidas");

        // Setup Producto
        producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombre("COCA COLA");
        producto.setPrecio(new BigDecimal("5.50"));
        producto.setStock(50);
        producto.setEstado(true);
        producto.setCategoria(categoria);

        // Setup Usuario
        usuario = new Usuario();
        usuario.setIdUsuario("user1");
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@mail.com");
        usuario.setRol(new Rol());

        // Setup Comanda
        comanda = new Comanda();
        comanda.setIdComanda(1);
        comanda.setFecha(LocalDateTime.now());
        comanda.setMesa(mesa);
        comanda.setMesero(usuario);
        comanda.setEstado(estado);

        DetalleComanda detalle = new DetalleComanda();
        detalle.setIdDetalleComanda(1);
        detalle.setComanda(comanda);
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(new BigDecimal("5.50"));
        detalle.setSubtotal(new BigDecimal("11.00"));

        comanda.setDetalles(Arrays.asList(detalle));

        // Setup Request DTO
        DetalleComandaDTO detalleDto = DetalleComandaDTO.builder()
                .productoId(1)
                .cantidad(2)
                .build();

        requestDto = ComandaDTO.builder()
                .mesaId(1)
                .meseroId("user1")
                .detalles(Arrays.asList(detalleDto))
                .build();
    }

    @Test
    @DisplayName("Crear comanda exitosamente")
    void save_Exitoso() {
        // Given
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));
        when(comandaMapper.toEntity(requestDto)).thenReturn(comanda);
        when(comandaRepository.save(any(Comanda.class))).thenReturn(comanda);
        when(comandaMapper.toDto(comanda)).thenReturn(requestDto);

        // When
        ComandaDTO response = comandaService.save(requestDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMesaId()).isEqualTo(1);
        assertThat(response.getEstadoId()).isEqualTo(1);
        verify(comandaRepository, times(1)).save(any(Comanda.class));
    }

    @Test
    @DisplayName("Crear comanda con mesa ocupada debe lanzar excepción")
    void save_MesaOcupada_DeberiaLanzarExcepcion() {
        // Given
        Estado estadoOcupada = new Estado();
        estadoOcupada.setIdEstado(2);
        estadoOcupada.setNombre("OCUPADA");
        mesa.setEstado(estadoOcupada);
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(comandaMapper.toEntity(requestDto)).thenReturn(comanda);

        // When & Then
        assertThatThrownBy(() -> comandaService.save(requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Mesa no disponible");
        verify(comandaRepository, never()).save(any(Comanda.class));
    }

    @Test
    @DisplayName("Crear comanda con mesa inexistente debe lanzar excepción")
    void save_MesaInexistente_DeberiaLanzarExcepcion() {
        // Given
        when(mesaRepository.findById(1)).thenReturn(Optional.empty());
        when(comandaMapper.toEntity(requestDto)).thenReturn(comanda);

        // When & Then
        assertThatThrownBy(() -> comandaService.save(requestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Mesa no encontrada");
        verify(comandaRepository, never()).save(any(Comanda.class));
    }

    @Test
    @DisplayName("Crear comanda sin detalles debe lanzar excepción")
    void save_SinDetalles_DeberiaLanzarExcepcion() {
        // Given
        requestDto.setDetalles(Collections.emptyList());
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));
        when(comandaMapper.toEntity(requestDto)).thenReturn(comanda);

        // When & Then
        assertThatThrownBy(() -> comandaService.save(requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("detalles");
        verify(comandaRepository, never()).save(any(Comanda.class));
    }

    @Test
    @DisplayName("Crear comanda con stock insuficiente debe lanzar excepción")
    void save_StockInsuficiente_DeberiaLanzarExcepcion() {
        // Given
        producto.setStock(1); // Solo hay 1 en stock pero se piden 2
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa));
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));
        when(comandaMapper.toEntity(requestDto)).thenReturn(comanda);

        // When & Then
        assertThatThrownBy(() -> comandaService.save(requestDto))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Stock insuficiente");
        verify(comandaRepository, never()).save(any(Comanda.class));
    }

    @Test
    @DisplayName("Actualizar estado de comanda exitosamente")
    void cambiarEstado_Exitoso() {
        // Given
        Estado estadoPreparacion = new Estado();
        estadoPreparacion.setIdEstado(2);
        estadoPreparacion.setNombre("EN PREPARACION");

        when(comandaRepository.findById(1)).thenReturn(Optional.of(comanda));
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estadoPreparacion));
        when(comandaRepository.save(any(Comanda.class))).thenReturn(comanda);
        when(comandaMapper.toDto(comanda)).thenReturn(requestDto);

        // When
        ComandaDTO response = comandaService.cambiarEstado(1, 2);

        // Then
        assertThat(response).isNotNull();
        verify(comandaRepository, times(1)).save(any(Comanda.class));
    }

    @Test
    @DisplayName("No se puede actualizar estado de comanda completada")
    void cambiarEstado_ComandaCompletada_DeberiaLanzarExcepcion() {
        // Given
        Estado estadoCompletada = new Estado();
        estadoCompletada.setIdEstado(5);
        estadoCompletada.setNombre("COMPLETADA");
        comanda.setEstado(estadoCompletada);
        when(comandaRepository.findById(1)).thenReturn(Optional.of(comanda));

        // When & Then
        assertThatThrownBy(() -> comandaService.cambiarEstado(1, 1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no se puede cambiar");
        verify(comandaRepository, never()).save(any(Comanda.class));
    }

    @Test
    @DisplayName("Obtener comanda por ID exitosamente")
    void findById_Exitoso() {
        // Given
        when(comandaRepository.findById(1)).thenReturn(Optional.of(comanda));
        when(comandaMapper.toDto(comanda)).thenReturn(requestDto);

        // When
        ComandaDTO response = comandaService.findById(1);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getIdComanda()).isEqualTo(1);
        assertThat(response.getEstadoId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Obtener comanda inexistente debe lanzar excepción")
    void findById_NoExiste_DeberiaLanzarExcepcion() {
        // Given
        when(comandaRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> comandaService.findById(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Comanda no encontrada");
    }

    @Test
    @DisplayName("Listar todas las comandas")
    void findAll_DeberiaRetornarLista() {
        // Given
        when(comandaRepository.findAll()).thenReturn(Arrays.asList(comanda));
        when(comandaMapper.toDtoList(Arrays.asList(comanda))).thenReturn(Arrays.asList(requestDto));

        // When
        List<ComandaDTO> response = comandaService.findAll();

        // Then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getIdComanda()).isEqualTo(1);
    }

    @Test
    @DisplayName("Obtener comandas por estado")
    void findByEstadoId_DeberiaRetornarLista() {
        // Given
        when(comandaRepository.findByEstadoIdEstado(1)).thenReturn(Arrays.asList(comanda));
        when(comandaMapper.toDtoList(Arrays.asList(comanda))).thenReturn(Arrays.asList(requestDto));

        // When
        List<ComandaDTO> response = comandaService.findByEstadoId(1);

        // Then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getEstadoId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Obtener comandas por mesa")
    void findByMesaId_DeberiaRetornarLista() {
        // Given
        when(comandaRepository.findByMesaIdMesa(1)).thenReturn(Arrays.asList(comanda));
        when(comandaMapper.toDtoList(Arrays.asList(comanda))).thenReturn(Arrays.asList(requestDto));

        // When
        List<ComandaDTO> response = comandaService.findByMesaId(1);

        // Then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getMesaId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Comanda completada debe liberar la mesa")
    void cambiarEstado_ACompletada_DeberiaLiberarMesa() {
        // Given
        Estado estadoOcupada = new Estado();
        estadoOcupada.setIdEstado(2);
        estadoOcupada.setNombre("OCUPADA");
        mesa.setEstado(estadoOcupada);

        Estado estadoCompletada = new Estado();
        estadoCompletada.setIdEstado(5);
        estadoCompletada.setNombre("COMPLETADA");

        Estado estadoDisponible = new Estado();
        estadoDisponible.setIdEstado(1);
        estadoDisponible.setNombre("DISPONIBLE");

        when(comandaRepository.findById(1)).thenReturn(Optional.of(comanda));
        when(estadoRepository.findById(5)).thenReturn(Optional.of(estadoCompletada));
        when(estadoRepository.findByNombreIgnoreCase("DISPONIBLE")).thenReturn(Optional.of(estadoDisponible));
        when(comandaRepository.save(any(Comanda.class))).thenReturn(comanda);
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);
        when(comandaMapper.toDto(comanda)).thenReturn(requestDto);

        // When
        ComandaDTO result = comandaService.cambiarEstado(1, 5);

        // Then
        assertThat(result).isNotNull();
        verify(mesaRepository, times(1)).save(any(Mesa.class));
    }
}