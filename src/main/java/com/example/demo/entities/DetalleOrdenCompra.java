package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
