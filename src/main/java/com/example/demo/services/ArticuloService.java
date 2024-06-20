package com.example.demo.services;

import java.time.Duration;
import java.util.List;

import com.example.demo.entities.Articulo;

public interface ArticuloService extends BaseService<Articulo,Long>{
    /*List<Articulo> findProductosAReponer() throws Exception;
    List<Articulo> findProductosFaltantes() throws Exception;*/
    Articulo agregarProveedorPredeterminado(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Long idArticulo, Long idProveedor) throws Exception;
    Articulo agregarModelo(Long idArticulo, Long idModelo) throws Exception;
    Articulo calcularCGIConProvPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta)throws Exception;
    Articulo calcularCGI (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor)throws Exception;
    Articulo AsignarUnProveedorAUnArticulo(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto, Long idArticulo, Long idProveedor) throws Exception;
}