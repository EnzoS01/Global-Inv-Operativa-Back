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
@Table(name= "ordenCompra")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrdenCompra extends Base{

    @Column(name = "fechaRealizacion")
    private Date fechaRealizacion;

    @Column(name = "total")
    private double total;

        @OneToMany
    @JoinTable(
            name = "OrdenCompra_DetalleOrdenCompra",
            joinColumns = @JoinColumn(name = "OrdenCompra_id"),
            inverseJoinColumns = @JoinColumn(name = "detalleOrdenCompra_id")
    )
    private List<DetalleOrdenCompra> detallesOrdenCompra= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_estadoOrdenCompra")
    private EstadoOrdenCompra estadoOrdenCompra;
}
