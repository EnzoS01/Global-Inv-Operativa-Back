package com.example.demo.repositories;

import com.example.demo.entities.OrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ProveedorArticulo;

import java.util.List;

@Repository
public interface ProveedorArticuloRepository extends BaseRepository<ProveedorArticulo,Long>  {

    @Query(value="select * from ProveedorArticulo p where p.fk_proveedor = :idProveedor and p.fk_articulo = :idArticulo", nativeQuery = true)
    ProveedorArticulo findByArticuloandProveedor(Long idProveedor, Long idArticulo);
}
