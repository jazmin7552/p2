package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Buscar por rol.idRol (derivado JPA: campo rol -> idRol)
    List<Usuario> findByRol_IdRol(Integer idRol);

    // Buscar por rol.nombre (case-insensitive)
    List<Usuario> findByRol_NombreIgnoreCase(String nombre);
}
