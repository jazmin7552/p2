package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.entity.DetalleComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleComandaRepository extends JpaRepository<DetalleComanda, Integer> {

    List<DetalleComanda> findByComandaIdComanda(Integer idComanda);

    List<DetalleComanda> findByProductoIdProducto(Integer idProducto);

    void deleteByComandaIdComanda(Integer idComanda);
}
