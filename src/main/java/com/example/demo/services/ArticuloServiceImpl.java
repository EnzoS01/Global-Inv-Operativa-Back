package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Modelo;
import com.example.demo.entities.Proveedor;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ModeloRepository;
import com.example.demo.repositories.ProveedorRepository;

import jakarta.transaction.Transactional;

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

    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository, ArticuloRepository articuloRepository) {
        super(baseRepository);
        this.articuloRepository = articuloRepository;
    }

    /*@Override
    @Transactional
    public List<Articulo> findProductosAReponer() throws Exception {
        try{
            List<Articulo> articulosAReponer= articuloRepository.ListadoProductosAReponer();
            return articulosAReponer;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


    @Override
    @Transactional
    public List<Articulo> findProductosFaltantes() throws Exception {
        try{
            List<Articulo> articulosFaltantes= articuloRepository.ListadoProductosFaltantes();
            return articulosFaltantes;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }


    }*/


    @Override
    public Articulo agregarProveedorPredeterminado(Long idArticulo, Long idProveedor) throws Exception {
        try{
            Articulo articulo= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
            Proveedor proveedor=proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            articulo.setProveedorPredeterminado(proveedor);
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

    
}
