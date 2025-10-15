package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {

    List<Mesa> findByEstadoIdEstado(Integer idEstado);

    List<Mesa> findByUbicacion(String ubicacion);

    List<Mesa> findByCapacidadGreaterThanEqual(Integer capacidad);
}
