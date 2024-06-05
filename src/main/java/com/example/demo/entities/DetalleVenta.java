package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private double subtota;

    @Column(name = "linea")
    private int linea;
}
