package com.example.demo.services;

import com.example.demo.entities.DetalleOrdenCompra;


public interface DetalleOrdenCompraService extends BaseService<DetalleOrdenCompra,Long>{
    public DetalleOrdenCompra setearArticulo(Long idDetalle, Long idArticulo) throws Exception;
    public DetalleOrdenCompra setearProveedor(Long idDetalle, Long idProveedor, int cantidad) throws Exception;
    public DetalleOrdenCompra DetallePorDefecto(Long idDetalle, Long idArticulo) throws Exception;

}
