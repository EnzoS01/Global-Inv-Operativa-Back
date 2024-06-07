package com.example.demo.services;

import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Venta;

public interface VentaService extends BaseService<Venta,Long>{
    public Venta agregarDetalleVenta(Long ventaid, DetalleVenta detalleVenta) throws Exception;

}
