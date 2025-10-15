package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "usuarios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rol {

    @Id
    @Column(name = "id_rol")
    @EqualsAndHashCode.Include
    private Integer idRol;

    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios = new ArrayList<>();
}