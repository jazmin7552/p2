package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Integer> {
    Optional<Telefono> findByNumero(String numero);

    boolean existsByNumero(String numero);
}
