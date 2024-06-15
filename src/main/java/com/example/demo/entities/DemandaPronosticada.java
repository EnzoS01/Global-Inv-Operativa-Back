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

    @Column(name = "nro_periodo_demanda_pronosticada")
    private int nroPeriodoDemandaPronosticada;

    @Column(name = "anio_demanda_pronosticada")
    private int anioDemandaPronosticada;

    @Column(name = "cantidad_demanda_real")
    private int cantidadDemandaReal;

    @ManyToOne
    @JoinColumn(name = "demanda_id")
    private Demanda demandaRealAsociada;

    @ManyToOne
    @JoinColumn(name = "pronostico_id", nullable = false)
    private Pronostico pronostico;
}
