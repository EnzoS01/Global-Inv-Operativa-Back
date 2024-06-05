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
@Table(name = "estadoOrdenCompra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoOrdenCompra extends Base{
    @Column(name = "nombreEstado")
    private String nombreEstado;

    @Column(name = "fechaAlta")
    private Date fechaAlta;

    @Column(name = "fechaBaja")
    private Date fechaBaja;
}
