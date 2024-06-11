package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Articulo;

public interface ArticuloService extends BaseService<Articulo,Long>{
    /*List<Articulo> findProductosAReponer() throws Exception;
    List<Articulo> findProductosFaltantes() throws Exception;*/
    Articulo agregarProveedorPredeterminado(Long idArticulo, Long idProveedor) throws Exception;
    Articulo agregarModelo(Long idArticulo, Long idModelo) throws Exception;

}
