package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.DetalleVenta;

public interface DetalleVentaService extends BaseService<DetalleVenta,Long>{

    public DetalleVenta agregarArticulo(Long detalleVentaId, Long articuloId) throws Exception;


}
