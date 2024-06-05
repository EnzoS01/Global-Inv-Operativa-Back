package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venta extends Base{

    @Column(name = "fechaRealizacion")
    private Date fechaRealizacion;

    @Column(name = "total")
    private double total;

    @Column(name = "nroFactura")
    private int nroFactura;
}
