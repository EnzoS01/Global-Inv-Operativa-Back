package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "demanda_pronosticada")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DemandaPronosticada extends Base {

    @Column(name = "cantidad_demandada_pronostico")
    private double cantidadDemandadaPronostico;

    @Column(name = "valor_error_pronostico_demanda_pronosticada")
    private double valorErrorPronosticoDemandaPronosticada;

    @ManyToOne
    @JoinColumn(name = "demanda_id")
    private Demanda demandaRealAsociada;

    @ManyToOne
    @JoinColumn(name = "modeloPrediccion")
    private ModeloPrediccion modeloPrediccion;
}
