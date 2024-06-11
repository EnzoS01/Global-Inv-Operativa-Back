package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import com.example.demo.enums.Model;

import java.io.Serializable;
import java.time.DateTimeException;
import java.util.Date;

@Entity
@Table(name = "articulo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Articulo extends Base {

    @Column(name = "nombreArticulo")
    private String nombreArticulo;

    @Column(name = "detalle", length = 1500)
    private String detalle;

    @Column(name = "fechaAlta")
    private Date fechaAlta;

    @Column(name = "fechaBaja")
    private Date fechaBaja;

    @Column(name = "loteOptimo")
    private int loteOptimo;

    @Column(name = "puntoPedido")
    private int puntoPedido;

    @Column(name = "stockSeguridad")
    private int stockSeguridad;

    @Column(name = "CGI")
    private float CGI;

    @Column(name = "CantActual")
    private int CantActual;

    @Column(name = "preciaVenta")
    private double precioVenta;

    @Enumerated(EnumType.STRING)
    private Model modelo;

    @ManyToOne
    @JoinColumn(name = "fk_proveedorPredeterminado")
    private Proveedor proveedorPredeterminado;

    //hola

}
