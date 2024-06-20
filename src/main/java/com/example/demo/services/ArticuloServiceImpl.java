package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Modelo;
import com.example.demo.entities.Proveedor;
import com.example.demo.entities.ProveedorArticulo;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DemandaRepository;
import com.example.demo.repositories.ModeloRepository;
import com.example.demo.repositories.ProveedorArticuloRepository;
import com.example.demo.repositories.ProveedorRepository;

import jakarta.transaction.Transactional;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService {
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private DemandaRepository demandaRepository;
    @Autowired
    private ProveedorArticuloRepository proveedorArticuloRepository;

    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository, ArticuloRepository articuloRepository) {
        super(baseRepository);
        this.articuloRepository = articuloRepository;
    }



    @Override
    public Articulo agregarProveedorPredeterminado(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Long idArticulo, Long idProveedor) throws Exception {
        try{
            Articulo articulo= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
            Proveedor proveedor=proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            articulo.setProveedorPredeterminado(proveedor);
            ProveedorArticulo pa= articulo.agregarUnProveedorPredeterminado(tiempoPedido,costoPedido,costoAlmacenamiento,costoProducto,proveedor,articulo);
            proveedorArticuloRepository.save(pa);
            articuloRepository.save(articulo);
            return articulo;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Articulo agregarModelo(Long idArticulo, Long idModelo) throws Exception {
        try{
            Articulo articulo= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
            Modelo modelo= modeloRepository.findById(idModelo).orElseThrow(() -> new RuntimeException("Modelo no encontrado"));
            articulo.setModelo(modelo);
            articuloRepository.save(articulo);
            return articulo;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
    @Override
    public Articulo AsignarUnProveedorAUnArticulo(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto, Long idArticulo, Long idProveedor) throws Exception {
        try{
            Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
            Proveedor p = proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            ProveedorArticulo pa=a.agregarUnProveedor(tiempoPedido,costoPedido,costoAlmacenamiento,costoProducto,p,a);
            proveedorArticuloRepository.save(pa);
            return a;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Articulo calcularCGIConProvPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo articulo = articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo,periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor proveedorPredeterminado= articulo.getProveedorPredeterminado();
        ProveedorArticulo proveedorArticulo=proveedorArticuloRepository.findByArticuloandProveedor(proveedorPredeterminado.getId(),idArticulo);
        int D = 0;
        float CGI= 0 ;
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        double P=proveedorArticulo.getCostoProducto();
        double Ca=proveedorArticulo.getCostoAlmacenamiento();
        int Q=articulo.getLoteOptimo();
        double Cp=proveedorArticulo.getCostoPedido();
        //CALCULO CGI
        CGI= (float) ((P*D) + (Ca*(Q/2)) + (Cp*(D/Q)));
        articulo.setCGI(CGI);
        articuloRepository.save(articulo);
        return articulo;
    }

    @Override
    public Articulo calcularCGI (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo articulo = articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo,periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor proveedor= proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        ProveedorArticulo proveedorArticulo=proveedorArticuloRepository.findByArticuloandProveedor(idProveedor,idArticulo);
        int D = 0;
        float CGI= 0 ;
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        double P=proveedorArticulo.getCostoProducto();
        double Ca=proveedorArticulo.getCostoAlmacenamiento();
        int Q=articulo.getLoteOptimo();
        double Cp=proveedorArticulo.getCostoPedido();
        //CALCULO CGI
        CGI= (float) ((P*D) + (Ca*(Q/2)) + (Cp*(D/Q)));
        articulo.setCGI(CGI);
        articuloRepository.save(articulo);
        return articulo;
    }

}
