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

        @Column(name = "error_aceptable")
        private double errorAceptable;

        @Column(name = "cantidad_periodos_historicos")
        private int cantidadPeriodosHistoricos;

        @Column(name = "periodoAPredecir")
        private int periodoAPredecir;

        @Column(name = "anioAPredecir")
        private int anioAPredecir;

        @OneToOne
        @JoinColumn(name = "demandaPronosticada_id")
        private DemandaPronosticada demandaPronosticadaOptima;

        @OneToMany
        @JoinTable(name = "Pronostico_DemandaPronosticada",
                        joinColumns = @JoinColumn(name = "pronostico_id"),
                        inverseJoinColumns = @JoinColumn(name = "demandaPronosticad_id"))
        private List<DemandaPronosticada> demandasPronosticada=new ArrayList<>();

        public void addDemandaPronosticada(DemandaPronosticada demandaPronosticada){
                this.demandasPronosticada.add(demandaPronosticada);
        }

        @ManyToOne
        @JoinColumn(name = "metodoError_id")
        private MetodoError metodoError;

        @ManyToOne
        @JoinColumn(name = "articulo_id")
        private Articulo articulo;

        @ManyToOne
        @JoinColumn(name = "ordenCompra_id")
        private OrdenCompra ordenCompra;


}
