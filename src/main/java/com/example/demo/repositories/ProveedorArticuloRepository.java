package com.example.demo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.ProveedorArticulo;


@Repository
public interface ProveedorArticuloRepository extends BaseRepository<ProveedorArticulo,Long>  {

    @Query(value = "SELECT * FROM PROVEEDOR_ARTICULO P  " +
               "WHERE P.fk_proveedor = :idProveedor AND P.fk_articulo = :idArticulo AND P.FECHA_BAJA IS NULL", nativeQuery = true)
    ProveedorArticulo findByArticuloAndProveedor(@Param("idProveedor") Long idProveedor, @Param("idArticulo") Long idArticulo); 

    @Query(value="select * from P where P.fk_articulo = :idArticulo and P.FECHA_BAJA is null", nativeQuery = true)
    ProveedorArticulo findByArticuloConFechaBajaNula( @Param("idArticulo") Long idArticulo);

    @Query(value="select * from P where P.fk_proveedor = :idProveedor and P.FECHA_BAJA is null", nativeQuery = true)
    ProveedorArticulo findByProveedorConFechaBajaNula( @Param("idProveedor") Long idProveedor);
    }



