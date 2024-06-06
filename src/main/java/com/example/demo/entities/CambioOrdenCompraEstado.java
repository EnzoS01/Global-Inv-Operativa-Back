package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "cambioOrdenCompraEstado")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CambioOrdenCompraEstado extends Base{

    @Column(name = "fechaCambio")
    private Date fechaCambio;

    @ManyToOne
    @JoinColumn(name = "fk_OrdenCompra")
    private OrdenCompra ordenCompra;

    @ManyToOne
    @JoinColumn(name = "fk_EstadoOrdenCompra")
    private EstadoOrdenCompra estadoOrdenCompra;

}
