package com.example.demo.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Proveedor;

@Repository
public interface ProveedorRepository extends BaseRepository<Proveedor,Long>{

    @Query("SELECT pa.proveedor FROM ProveedorArticulo pa WHERE pa.articulo.id = :idArticulo")
    List<Proveedor> findProveedoresByArticuloId(@Param("idArticulo") Long idArticulo);

}
