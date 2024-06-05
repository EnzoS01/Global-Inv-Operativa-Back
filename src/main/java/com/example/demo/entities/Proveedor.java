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
@Table(name= "proveedor")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Proveedor extends Base{

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "telefono")
    private double telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "mail")
    private String mail;

    @Column(name = "fechaAlta")
    private Date fechaAlta;

    @Column(name = "fechaBaja")
    private Date fechaBaja;
}
