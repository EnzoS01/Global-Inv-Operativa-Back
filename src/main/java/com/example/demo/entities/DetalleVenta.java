package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalleVenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta extends Base {

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "linea")
    private int linea;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    private Articulo articulo;
}
