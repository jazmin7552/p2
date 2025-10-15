package producto.application.service;

import com.example.demo.application.service.ProductoService;
import com.example.demo.domain.dto.ProductoDTO;
import com.example.demo.domain.entity.Categoria;
import com.example.demo.domain.entity.Producto;
import com.example.demo.domain.mapper.ProductoMapper;
import com.example.demo.infrastructure.persistence.repository.CategoriaRepository;
import com.example.demo.infrastructure.persistence.repository.ProductoRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    private Categoria categoria;
    private Producto producto;
    private ProductoDTO requestDto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNombre("Bebidas");

        producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombre("COCA COLA");
        producto.setPrecio(new BigDecimal("5.50"));
        producto.setStock(50);
        producto.setEstado(true);
        producto.setCategoria(categoria);

        requestDto = ProductoDTO.builder()
                .idProducto(1)
                .nombre("COCA COLA")
                .precio(new BigDecimal("5.50"))
                .stock(50)
                .estado(true)
                .categoriaId(1)
                .build();
    }

    @Test
    @DisplayName("Crear producto exitosamente")
    void save_Exitoso() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(productoRepository.existsByNombreIgnoreCase(anyString())).thenReturn(false);
        when(productoMapper.toEntity(requestDto)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(requestDto);

        ProductoDTO response = productoService.save(requestDto);

        assertThat(response).isNotNull();
        assertThat(response.getNombre()).isEqualTo("COCA COLA");

        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("Crear producto con nombre duplicado debe lanzar excepción")
    void save_NombreDuplicado_DeberiaLanzarExcepcion() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(productoRepository.existsByNombreIgnoreCase(anyString())).thenReturn(true);

        assertThatThrownBy(() -> productoService.save(requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Ya existe un producto con el nombre");

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("Obtener producto por ID exitosamente")
    void findById_Exitoso() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoMapper.toDto(producto)).thenReturn(requestDto);

        ProductoDTO response = productoService.findById(1);

        assertThat(response).isNotNull();
        assertThat(response.getIdProducto()).isEqualTo(1);

        verify(productoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Buscar producto por ID no existente debe lanzar excepción")
    void findById_NoExistente_DeberiaLanzarExcepcion() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producto no encontrado");

        verify(productoRepository, times(1)).findById(99);
    }
}