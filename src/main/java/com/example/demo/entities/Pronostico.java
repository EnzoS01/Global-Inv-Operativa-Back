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
@Table(name = "pronostico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pronostico extends Base{
    @Column(name = "numPronostico")
    private int numPronostico;

    @Column(name = "nombrePronostico")
    private String nombrePronostico;

    @Column(name = "errorAceptablePronostico")
    private double errorAceptablePronostico;

    @Column(name = "cantidadPeriodosAPredecir")
    private int cantidadPeriodosAPredecir;

    @Column(name = "cantidadPeriodosHistoricos")
    private int cantidadPeriodosHistoricos;

    @ManyToOne
    @JoinColumn(name = "fk_ordenCompra")
    private OrdenCompra ordenCompra;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    private Articulo articulo;

    @OneToMany
    @JoinTable(
            name = "Pronostico_PronosticoDemanda",
            joinColumns = @JoinColumn(name = "pronostico_id"),
            inverseJoinColumns = @JoinColumn(name = "pronosticoDemanda_id")

    )
    private List<PronosticoDemanda> pronosticosDemandas=new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "Pronostico_DemandaPronosticada",
            joinColumns = @JoinColumn(name = "pronostico_id"),
            inverseJoinColumns = @JoinColumn(name = "demandaPronosticada_id")

    )
    private List<DemandaPronosticada> demandaPronosticada=new ArrayList<>();
}
