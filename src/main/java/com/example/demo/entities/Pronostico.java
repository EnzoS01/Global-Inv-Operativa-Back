package com.example.demo.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

        @Column(name = "cantidad_periodos_historicos")
        private int cantidadPeriodosHistoricos;

        @OneToOne
        @JoinColumn(name = "demandaPronosticada_id")
        private DemandaPronosticada demandaPronosticada;

        @ManyToOne
        @JoinColumn(name = "articulo_id")
        private Articulo articulo;

        @ManyToOne
        @JoinColumn(name = "ordenCompra_id")
        private OrdenCompra ordenCompra;


}
