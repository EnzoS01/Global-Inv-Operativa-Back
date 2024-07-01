package com.example.demo.services;
import java.util.List;
import com.example.demo.DTO.ArticuloDTO;
import com.example.demo.entities.Articulo;

public interface ArticuloService extends BaseService<Articulo,Long>{
    Articulo agregarProveedorPredeterminado(Long idArticulo, Long idProveedor) throws Exception; 
    //METODO NO USADO EN EL FRONT
    Articulo agregarProveedorPredeterminado(int tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Long idArticulo, Long idProveedor) throws Exception;
    Articulo agregarModelo(Long idArticulo, Long idModelo) throws Exception;
    //METODO NO USADO EN EL FRONT
    Articulo calcularCGIConProvPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta)throws Exception;
    Articulo calcularCGI (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor)throws Exception;
    Articulo AsignarUnProveedorAUnArticulo(int tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto, Long idArticulo, Long idProveedor) throws Exception;
    //METODO NO USADO EN EL FRONT
    Articulo ModeloConProveedorPredeterminado(Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, float DDesvEstandar, double Z)throws Exception;
    Articulo ModeloConProveedor (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor, float DDesvEstandar, double Z)throws Exception;
    List<ArticuloDTO> ListadoDeArticulosFaltantes() throws Exception;
    List<ArticuloDTO> ListadoDeArticulosAReponer() throws Exception;
    Articulo findByname(String nombre) throws Exception;
}