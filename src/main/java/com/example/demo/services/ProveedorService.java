package com.example.demo.services;

import com.example.demo.entities.Proveedor;

public interface ProveedorService extends BaseService<Proveedor,Long>{
    Proveedor AsignarUnArtiucloAUnProveedor(Long idArticulo, Long idProveedor) throws Exception;
}
