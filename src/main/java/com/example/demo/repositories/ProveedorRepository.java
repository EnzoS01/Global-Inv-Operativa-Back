package com.example.demo.repositories;

import com.example.demo.entities.Articulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Proveedor;

import java.util.List;

@Repository
public interface ProveedorRepository extends BaseRepository<Proveedor,Long>{

    @Query(value = "select * from  PROVEEDOR p where p.nombre = :nombre ", nativeQuery = true)
    Proveedor findByName(@Param("nombre") String nombre);

    @Query(value = "SELECT pa.proveedor FROM ProveedorArticulo pa WHERE pa.articulo.id = :idArticulo")
    List<Proveedor> findProveedoresByArticuloId(@Param("idArticulo") Long idArticulo);


}
