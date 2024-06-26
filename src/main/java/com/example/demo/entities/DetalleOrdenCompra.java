package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "detalleOrdenCompra")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetalleOrdenCompra extends Base{

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "subtotal")
    private float subtotal;

    @Column(name = "linea")
    private int linea;

    @ManyToOne
    @JoinColumn(name = "fk_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    private Articulo articulo;
}
