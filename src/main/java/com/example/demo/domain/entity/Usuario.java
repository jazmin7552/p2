package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "rol", "telefonos", "comandasComoMesero", "comandasComoCocinero" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @Column(name = "id_usuario", length = 20)
    @EqualsAndHashCode.Include
    private String idUsuario;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol", nullable = false)
    private Rol rol;

    @ManyToMany
    @JoinTable(name = "usuario_telefono", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_telefono"))
    @Builder.Default
    private List<Telefono> telefonos = new ArrayList<>();

    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comanda> comandasComoMesero = new ArrayList<>();

    @OneToMany(mappedBy = "cocinero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comanda> comandasComoCocinero = new ArrayList<>();
}
