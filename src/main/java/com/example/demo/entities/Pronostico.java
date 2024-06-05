package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    @Column(name = "fechaInicioPronostico")
    private Date fechaInicioPronostico;

    @Column(name = "fechaFinPronostico")
    private Date fechaFinPronostico;

    @Column(name = "errorAceptablePronostico")
    private double errorAceptablePronostico;

    @Column(name = "periodoPronostico")
    private int periodoPronostico;

    @Column(name = "errorDiferenciaDemandaRealPronosticada")
    private float errorDiferenciaDemandaRealPronosticada;
}
