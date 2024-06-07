package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pronosticoDemanda")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PronosticoDemanda extends Base{

    @Column(name = "numeroiPronosticoDemanda")
    private int numeroiPronosticoDemanda;

    @ManyToOne
    @JoinColumn(name = "fk_demanda")
    private Demanda demanda;
}
