package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "demandaPronosticada")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DemandaPronosticada extends Base{
    @Column(name = "cantidadDemandadaPronostico")
    private int cantidadDemandadaPronostico;

    @Column(name = "valorErrorPronosticoDemandaPronosticada")
    private float valorErrorPronosticoDemandaPronosticada;

    @Column(name = "nroPeriodo")
    private int nroPeriodo;

    @Column(name = "año")
    private int año;


}
