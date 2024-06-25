package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "proveedorArticulo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProveedorArticulo extends Base{
    @Column(name = "fechaBaja")
    private Date fechaBaja;

    @Column(name = "fechaAlta")
    private LocalDate fechaAlta;

    @Column(name = "tiempoPedido")
    private int tiempoPedido;

    @Column(name = "costoPedido")
    private float costoPedido;

    @Column(name = "costoAlmacenamiento")
    private float costoAlmacenamiento;

    @Column(name = "costoProducto")
    private float costoProducto;

    @ManyToOne
    @JoinColumn(name = "fk_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    private Articulo articulo;

    public ProveedorArticulo(int tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Proveedor proveedor, Articulo articulo){
        this.fechaBaja=null;
        this.fechaAlta=LocalDate.now();
        this.tiempoPedido=tiempoPedido;
        this.costoPedido=costoPedido;
        this.costoAlmacenamiento=costoAlmacenamiento;
        this.costoProducto=costoProducto;
        this.proveedor=proveedor;
        this.articulo=articulo;
    }
}
