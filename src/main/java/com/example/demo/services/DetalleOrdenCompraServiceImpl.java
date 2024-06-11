package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.DetalleOrdenCompra;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Proveedor;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DetalleOrdenCompraRepository;
import com.example.demo.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class DetalleOrdenCompraServiceImpl extends BaseServiceImpl<DetalleOrdenCompra,Long> implements DetalleOrdenCompraService {

    @Autowired
    private DetalleOrdenCompraRepository DetalleRepo;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ArticuloRepository ArticuloRepo;

    public DetalleOrdenCompraServiceImpl(BaseRepository<DetalleOrdenCompra, Long> baseRepository) {
        super(baseRepository);
    }

    @Transactional
    public DetalleOrdenCompra setearArticulo(Long idDetalle, Long idArticulo){
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        d.setArticulo(a);
        return DetalleRepo.save(d);
    }

    @Transactional
    public DetalleOrdenCompra setearProveedor(Long idDetalle, Long idProveedor){
        Proveedor p = proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("No se encontro el proveedor"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        d.setProveedor(p);
        return DetalleRepo.save(d);
    }

    //standby el metodo para recuperar la cantidad optima a pedir
}
