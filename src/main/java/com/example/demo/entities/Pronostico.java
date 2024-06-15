package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pronostico")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pronostico extends Base {

        @Column(name = "nombre_pronostico", nullable = false)
        private String nombrePronostico;

        @Column(name = "error_aceptable")
        private double errorAceptable;

        @Column(name = "cantidad_periodos_a_predecir")
        private int cantidadPeriodosAPredecir;

        @Column(name = "cantidad_periodos_historicos")
        private int cantidadPeriodosHistoricos;

        @OneToMany(mappedBy = "pronostico", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<DemandaPronosticada> demandaPronosticada = new ArrayList<>();

        @ManyToOne
        @JoinColumn(name = "articulo_id", nullable = false)
        private Articulo articulo;
}
