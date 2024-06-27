package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

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
    private double CGI;

    @Column(name = "CantActual")
    private int CantActual;

    @Column(name = "precioVenta")
    private double precioVenta;

    @Column(name = "periodo")
    private int periodo;

    @ManyToOne
    @JoinColumn(name = "fk_modelo")
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "fk_proveedorPredeterminado")
    private Proveedor proveedorPredeterminado;

    public ProveedorArticulo agregarUnProveedor(int tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Proveedor proveedor, Articulo articulo){
        ProveedorArticulo pa = new ProveedorArticulo(tiempoPedido, costoPedido, costoAlmacenamiento, costoProducto, proveedor, articulo);
        return pa;
    }

    public ProveedorArticulo agregarUnProveedorPredeterminado(int tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Proveedor proveedor, Articulo articulo){
        ProveedorArticulo pa = new ProveedorArticulo(tiempoPedido, costoPedido, costoAlmacenamiento, costoProducto, proveedor, articulo);
        this.proveedorPredeterminado=proveedor;
        return pa;
    }


}
