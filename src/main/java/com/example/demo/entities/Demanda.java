package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "demanda")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Demanda extends Base {

    @Column(name = "numPeriodo")
    private int numPeriodo;

    @Column(name = "anio")
    private int anio;

    @Column(name = "cantTotalDemanda")
    private int cantTotalDemanda;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    private Articulo articulo;

    @OneToMany
    @JoinTable(name = "Demanda_DetalleVenta",
            joinColumns = @JoinColumn(name = "Demanda_id"),
            inverseJoinColumns = @JoinColumn(name = "DetalleVenta_id"))
    private List<DetalleVenta> detallesVenta = new ArrayList<>();

    public void addDetallesVenta(DetalleVenta detalleVenta) {
        detallesVenta.add(detalleVenta);
    }

    /*
    public Demanda(int numPeriodo, int anio, Articulo articulo, List<Venta> ventas) {
        this.numPeriodo = numPeriodo;
        this.anio = anio;
        this.articulo = articulo;
        this.detallesVenta = obtenerDetallesVentaRelevantes(ventas, articulo, numPeriodo, anio);
        this.cantTotalDemanda = this.detallesVenta.stream()
                .mapToInt(DetalleVenta::getCantidad)
                .sum();
    }

    private List<DetalleVenta> obtenerDetallesVentaRelevantes(List<Venta> ventas, Articulo articulo, int numPeriodo,
            int anio) {
        return ventas.stream()
                .filter(venta -> {
                    LocalDate fecha = convertToLocalDate(venta.getFechaRealizacion());
                    return fecha.getMonthValue() == numPeriodo && fecha.getYear() == anio;
                })
                .flatMap(venta -> venta.getDetallesVenta().stream())
                .filter(detalle -> detalle.getArticulo().equals(articulo))
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

     */
}
