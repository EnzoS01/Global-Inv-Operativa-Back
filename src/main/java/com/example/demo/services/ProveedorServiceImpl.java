package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Proveedor;
import com.example.demo.entities.ProveedorArticulo;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ProveedorArticuloRepository;
import com.example.demo.repositories.ProveedorRepository;
@Service
public class ProveedorServiceImpl extends BaseServiceImpl<Proveedor,Long> implements ProveedorService{
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private ProveedorArticuloRepository proveedorArticuloRepository;
    
    public ProveedorServiceImpl(BaseRepository<Proveedor,Long> baseRepository, ProveedorRepository proveedorRepository) {
        super(baseRepository);
        this.proveedorRepository= proveedorRepository;
    }
    public Proveedor AsignarUnArtiucloAUnProveedor(Long idArticulo, Long idProveedor) throws Exception{
        try{
        Proveedor p = proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Articulo a = articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        ProveedorArticulo pa = proveedorArticuloRepository.findByProveedorConFechaBajaNula(idProveedor);
        pa.setArticulo(a);
        proveedorArticuloRepository.save(pa);
        return p;} catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }



}
    




