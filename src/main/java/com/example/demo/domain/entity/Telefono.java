package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "telefonos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuarios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    @EqualsAndHashCode.Include
    private Integer idTelefono;

    @Column(name = "numero", length = 15, nullable = false, unique = true)
    private String numero;

    @ManyToMany(mappedBy = "telefonos")
    @Builder.Default
    private List<Usuario> usuarios = new ArrayList<>();
}
