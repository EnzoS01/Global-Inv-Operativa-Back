package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "demanda")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Demanda extends Base{

    @Column(name = "numDemanda")
    private int numDemanda;

    @Column(name = "numPeriodo")
    private int numPeriodo;

    @Column(name = "año")
    private int año;

    @Column(name = "cantTotalDemanda")
    private int cantTotalDemanda;
}
