package com.example.demo.services;

import aj.org.objectweb.asm.Opcodes;
import com.example.demo.entities.Articulo;
import com.example.demo.repositories.ArticuloRepository;
import jakarta.transaction.Transactional;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloService implements BaseService<Articulo> {

    private ArticuloRepository articuloRepository;

    public ArticuloService(ArticuloRepository articuloRepository){
        this.articuloRepository = articuloRepository;
    }

    @Override
    @Transactional
    public List<Articulo> findAll() throws Exception {
        try {
            List<Articulo> entities= articuloRepository.findAll();
            return entities;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    @Override
    @Transactional
    public Articulo findById(Long id) throws Exception {
        try {
            Optional<Articulo> entityOptional= articuloRepository.findById(id);
            return entityOptional.get();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Articulo save(Articulo entity) throws Exception {
        try {
            entity= articuloRepository.save(entity);
            return entity;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Articulo update(Long id, Articulo entity) throws Exception {
        try {
            Optional<Articulo> entityOptional= articuloRepository.findById(id);
            Articulo articulo= entityOptional.get();
            articulo = articuloRepository.save(articulo);
            return articulo;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws Exception {
        try {
            if (articuloRepository.existsById(id)) {
                articuloRepository.deleteById(id);
                return true;
            }else{
                throw new Exception();
            }

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
