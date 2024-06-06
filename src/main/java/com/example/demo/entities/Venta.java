package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany
    @JoinTable(
            name = "Venta_DetallesVenta",
            joinColumns = @JoinColumn(name = "Venta_id"),
            inverseJoinColumns = @JoinColumn(name = "DetalleVenta_id")
    )
    private List<DetalleVenta> DetallesVenta= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="fk_cliente")
    private Cliente cliente;

}
