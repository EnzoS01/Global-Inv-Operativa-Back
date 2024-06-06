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
@Table(name= "modelo")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Modelo extends Base{
    @Column(name = "nombreModelo")
    private String nombreModelo;

    @Column(name = "fechaBaja")
    private Date fechaBaja;

    @Column(name = "fechaAlta")
    private Date fechaAlta;
}
