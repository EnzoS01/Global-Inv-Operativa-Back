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

    

    @Transactional
    public DetalleOrdenCompra setearArticulo(Long idDetalle, Long idArticulo){
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        d.setArticulo(a);
        DetalleRepo.save(d);
        return d;
    }

    @Transactional
    public DetalleOrdenCompra setearProveedor(Long idDetalle, Long idProveedor){
        Proveedor p = proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("No se encontro el proveedor"));
        DetalleOrdenCompra d = DetalleRepo.findById(idDetalle).orElseThrow(() -> new RuntimeException("No se encontro el detalle"));

        d.setProveedor(p);
        DetalleRepo.save(d);
        return d;
    }

    //standby el metodo para recuperar la cantidad optima a pedir
}
