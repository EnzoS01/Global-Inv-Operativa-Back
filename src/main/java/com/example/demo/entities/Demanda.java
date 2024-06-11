package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "demanda")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Demanda extends Base{

    @Column(name = "numPeriodo")
    private int numPeriodo;

    @Column(name = "año")
    private int año;

    @Column(name = "cantTotalDemanda")
    private int cantTotalDemanda;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    private Articulo articulo;

    @OneToMany
    @JoinTable(
            name = "Demanda_DetalleVenta",
            joinColumns = @JoinColumn(name = "Demanda_id"),
            inverseJoinColumns = @JoinColumn(name = "DetalleVenta_id")
    )
    private List<DetalleVenta> detallesVenta= new ArrayList<>();
}
