package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.entity.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Integer> {

    // Búsquedas por relaciones
    List<Comanda> findByMesaIdMesa(Integer idMesa);

    List<Comanda> findByMeseroIdUsuario(String idMesero);

    List<Comanda> findByCocineroIdUsuario(String idCocinero);

    List<Comanda> findByEstadoIdEstado(Integer idEstado);

    // ⭐ NUEVO - Para obtener comandas activas (todas excepto un estado específico)
    List<Comanda> findByEstadoIdEstadoNot(Integer idEstado);

    // Búsquedas por fecha
    List<Comanda> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT c FROM Comanda c WHERE c.fecha >= :fechaInicio AND c.fecha <= :fechaFin")
    List<Comanda> findComandasEnRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    // ⭐ NUEVOS - Métodos adicionales útiles para reportes y estadísticas
    @Query("SELECT c FROM Comanda c WHERE c.mesa.idMesa = :idMesa AND c.estado.idEstado != :idEstado")
    List<Comanda> findComandasActivasPorMesa(@Param("idMesa") Integer idMesa, @Param("idEstado") Integer idEstado);

    @Query("SELECT COUNT(c) FROM Comanda c WHERE c.estado.idEstado = :idEstado")
    Long countByEstadoIdEstado(@Param("idEstado") Integer idEstado);

    @Query("SELECT c FROM Comanda c ORDER BY c.fecha DESC")
    List<Comanda> findAllOrderByFechaDesc();

    @Query("SELECT c FROM Comanda c WHERE DATE(c.fecha) = CURRENT_DATE ORDER BY c.fecha DESC")
    List<Comanda> findComandasDelDia();
}