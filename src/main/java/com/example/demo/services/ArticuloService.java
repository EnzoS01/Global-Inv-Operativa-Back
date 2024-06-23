package com.example.demo.services;

import java.time.Duration;
import java.util.List;

import com.example.demo.DTO.ArticuloDTO;
import com.example.demo.entities.Articulo;

public interface ArticuloService extends BaseService<Articulo,Long>{
    Articulo agregarProveedorPredeterminado(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Long idArticulo, Long idProveedor) throws Exception;
    Articulo agregarModelo(Long idArticulo, Long idModelo) throws Exception;
    Articulo calcularCGIConProvPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta)throws Exception;
    Articulo calcularCGI (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor)throws Exception;
    Articulo AsignarUnProveedorAUnArticulo(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto, Long idArticulo, Long idProveedor) throws Exception;
    Articulo LoteFijoConProveedor (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor, float DPromedio, float DDesvEstandar, double Z)throws Exception;
    Articulo LoteFijoConProveedorPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, float DPromedio, float DDesvEstandar, double Z)throws Exception;
    Articulo IntervaloFijoConProveedor (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor, float DPromedio, float DDesvEstandar, double Z, Duration periodo)throws Exception;
    Articulo IntervaloFijoConProveedorPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, float DPromedio, float DDesvEstandar, double Z, Duration periodo)throws Exception;
    List<ArticuloDTO> ListadoDeArticulosFaltantes() throws Exception;
    List<ArticuloDTO> ListadoDeArticulosAReponer() throws Exception;
}