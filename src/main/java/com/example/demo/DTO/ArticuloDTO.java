package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticuloDTO {
    public String nombreArticulo;
    public int loteOptimo;
    public int puntoPedido;
    public int stockSeguridad;
    public int CantActual;

    public ArticuloDTO(String nombreArticulo, int loteOptimo, int puntoPedido, int stockSeguridad, int CantActual) {
        this.nombreArticulo = nombreArticulo;
        this.loteOptimo = loteOptimo;
        this.puntoPedido = puntoPedido;
        this.stockSeguridad = stockSeguridad;
        this.CantActual = CantActual;
    }
    public ArticuloDTO(){
    }
}
