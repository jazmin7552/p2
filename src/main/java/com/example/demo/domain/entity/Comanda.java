package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comandas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "mesa", "mesero", "cocinero", "estado", "detalles" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comanda")
    @EqualsAndHashCode.Include
    private Integer idComanda;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa", nullable = false)
    private Mesa mesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mesero", nullable = false)
    private Usuario mesero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cocinero")
    private Usuario cocinero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado", nullable = false)
    private Estado estado;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DetalleComanda> detalles = new ArrayList<>();

    // Método helper para mantener sincronización bidireccional
    public void addDetalle(DetalleComanda detalle) {
        detalles.add(detalle);
        detalle.setComanda(this);
    }

    public void removeDetalle(DetalleComanda detalle) {
        detalles.remove(detalle);
        detalle.setComanda(null);
    }
}