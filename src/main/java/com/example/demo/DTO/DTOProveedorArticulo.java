package com.example.demo.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class DTOProveedorArticulo {
    public Long IdArticulo;
    public String nombreArticulo;
    public String detalle;
    public int loteOptimo;
    public DTOProveedor ProveedorPredeterminado;
    public List<DTOProveedor> listaproveedores = new ArrayList<>();


}
