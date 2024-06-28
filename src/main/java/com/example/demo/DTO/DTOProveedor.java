package com.example.demo.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class DTOProveedor {
    public Long IdProveedor;
    public int tiempoPedido;
    public float costoPedido;
    public String nombre;
    public double telefono;
    public String direccion;
    public String mail;
}
