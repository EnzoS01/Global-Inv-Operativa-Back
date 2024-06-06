package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.Date;

@Entity
@Table(name = "proveedorArticulo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProveedorArticulo extends Base{
    @Column(name = "fechaHoraBaja")
    private Date fechaHoraBaja;

    @Column(name = "fechaHoraAlta")
    private Date fechaHoraAlta;

    @Column(name = "tiempoPedido")
    private Duration tiempoPedido;    // le puse Duration por chat, pero a chequear

    @Column(name = "costoPedido")
    private float costoPedido;

    @Column(name = "costoAlmacenamiento")
    private float costoAlmacenamiento;

    @Column(name = "costoProducto")
    private Double costoProducto;

    @ManyToOne
    @JoinColumn(name = "fk_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    private Articulo articulo;
}
