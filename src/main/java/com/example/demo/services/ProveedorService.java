package com.example.demo.services;
import java.util.List;
import java.util.ArrayList;
import com.example.demo.entities.Proveedor;

public interface ProveedorService extends BaseService<Proveedor,Long>{
    Proveedor AsignarUnArtiucloAUnProveedor(Long idArticulo, Long idProveedor) throws Exception;
    List<Proveedor> ObtenerProveedoresQueProveenUnArticulo(Long idArticulo) throws Exception;
}
