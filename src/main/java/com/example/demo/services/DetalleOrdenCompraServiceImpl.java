package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class DetalleOrdenCompraServiceImpl extends BaseServiceImpl<DetalleOrdenCompra,Long> implements DetalleOrdenCompraService {

    public DetalleOrdenCompraServiceImpl(BaseRepository<DetalleOrdenCompra, Long> baseRepository, DetalleOrdenCompraRepository DetalleRepo) {
        super(baseRepository);
        this.DetalleRepo=DetalleRepo;
    }

    @Autowired
    private DetalleOrdenCompraRepository DetalleRepo;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ArticuloRepository ArticuloRepo;

    @Autowired
    private ProveedorArticuloRepository ProveedorArticuloRepo;

    
    @Transactional
    public DetalleOrdenCompra DetallePorDefecto(Long idDetalle, Long idArticulo){
        //uso el proveedor predeterminado
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));
        Proveedor predeterminado = proveedorRepository.findById(a.getProveedorPredeterminado().getId()).orElseThrow(() -> new RuntimeException("No se encontro el proveedor"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        //metodo de inventario para calcular el lote optimo
        int loteoptimo = 4; /*Aca iria la llamada con ArticuloServiceImpl.CalcularModelo(a.getId());*/

        ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloandProveedor(predeterminado.getId(), a.getId());

        d.setCantidad(loteoptimo);
        d.setSubtotal(loteoptimo * PA.getCostoPedido());
        d.setArticulo(a);
        d.setProveedor(predeterminado);

        DetalleRepo.save(d);
        return d;
    }


    //este metodo esta medio decorativo
    @Transactional
    public DetalleOrdenCompra setearArticulo(Long idDetalle, Long idArticulo){
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        //metodo de inventario para calcular el lote optimo
        int loteoptimo = 4; /*Aca iria la llamada con ArticuloServiceImpl.CalcularModelo(a.getId());*/

        d.setCantidad(loteoptimo);
        d.setArticulo(a);
        DetalleRepo.save(d);
        return d;
    }

    //actualizo el detalle con el proveedor q eligio y la cantidad que ingreso
    @Transactional
    public DetalleOrdenCompra setearProveedor(Long idDetalle, Long idProveedor, int cantidad){
        Proveedor p = proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("No se encontro el proveedor"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));
        Articulo a = d.getArticulo();

        ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloandProveedor(p.getId(), a.getId());
        d.setSubtotal(cantidad * PA.getCostoPedido());
        d.setCantidad(cantidad);
        d.setProveedor(p);
        DetalleRepo.save(d);
        return d;
    }

    /*
    @Transactional
    public DetalleOrdenCompra finalizarCreacion(Long idDetalle,int cantidad){
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));
        d.setCantidad(cantidad);
        DetalleRepo.save(d);
        return d;
    }*/
    //standby el metodo para recuperar la cantidad optima a pedir
}
