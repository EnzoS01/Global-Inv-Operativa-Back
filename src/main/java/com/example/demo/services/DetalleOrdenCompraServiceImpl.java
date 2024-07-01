package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private EstadoOrdenCompraRepository estadoOrdenRepository;

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;


    public DetalleOrdenCompra nuevoDetalle(DetalleOrdenCompra Detalle){
        DetalleOrdenCompra nuevo = new DetalleOrdenCompra();
        nuevo.setSubtotal(Detalle.getSubtotal());
        nuevo.setCantidad(Detalle.getCantidad());
        nuevo.setLinea(Detalle.getLinea());
        nuevo.setArticulo(Detalle.getArticulo());
        DetalleRepo.save(nuevo);
        return nuevo;
    }
    
    @Transactional
    public DetalleOrdenCompra DetallePorDefecto(Long idDetalle, Long idArticulo){
        //uso el proveedor predeterminado
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));
        Proveedor predeterminado = proveedorRepository.findById(a.getProveedorPredeterminado().getId()).orElseThrow(() -> new RuntimeException("No se encontro el proveedor"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        EstadoOrdenCompra estadoOrdenCompraPen = estadoOrdenRepository.findByName("Pendiente");
        // .orElseThrow(() -> new RuntimeException("Estado Orden de compra no encontrada"));

        EstadoOrdenCompra estadoOrdenCompraEnv = estadoOrdenRepository.findByName("Enviada");

        // Verifico si existe una orden para el artículo
        List<OrdenCompra> ordenesActivas = ordenCompraRepository.findByArticuloAndEstado(a.getId(), estadoOrdenCompraPen.getId(), estadoOrdenCompraEnv.getId());

        if (!ordenesActivas.isEmpty()) {
            throw new RuntimeException("Ya existe/n"+ ordenesActivas.toArray().length + " orden/es de compra activa/s para el artículo " + a.getNombreArticulo());
        }


        ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloAndProveedor(predeterminado.getId(), a.getId());

        d.setCantidad(a.getLoteOptimo());
        d.setSubtotal(a.getLoteOptimo() * PA.getCostoPedido());
        d.setArticulo(a);
        d.setProveedor(predeterminado);

        return DetalleRepo.save(d);
    }


    /*//este metodo esta medio decorativo, ignorarlo
    @Transactional
    public DetalleOrdenCompra setearArticulo(Long idDetalle, Long idArticulo){
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        d.setCantidad(a.getLoteOptimo());
        d.setArticulo(a);
        DetalleRepo.save(d);
        return d;
    }*/

    //actualizo el detalle con el proveedor q eligio y la cantidad que ingreso
    @Transactional
    public DetalleOrdenCompra setearProveedor(Long idDetalle, Long idProveedor, int cantidad){
        Proveedor p = proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("No se encontro el proveedor"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));
        Articulo a = d.getArticulo();

        ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloAndProveedor(p.getId(), a.getId());
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
