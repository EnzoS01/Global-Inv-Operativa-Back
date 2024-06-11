package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService {
    @Autowired
    private ArticuloRepository articuloRepository;

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


    }
    public Articulo agregarProveedorPredeterminado(Long idProveedor, Long idArticulo) throws Exception{
        Articulo articulo = articuloRepository.getById(idArticulo)
    };*/

}
