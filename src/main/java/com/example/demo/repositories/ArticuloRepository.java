package com.example.demo.repositories;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {
    @Query(value = "select * from  articulo a where a.STOCK_SEGURIDAD >= a.CANT_ACTUAL ", nativeQuery = true)
    List<Articulo> articulosFaltantes();   

    @Query(value = "select * from  articulo a where a.PUNTO_PEDIDO >= a.CANT_ACTUAL ", nativeQuery = true)
    List<Articulo> articulosAReponer();

    @Query(value = "select * from  articulo a where a.nombreArticulo = :nombre ", nativeQuery = true)
    Articulo findByName(@Param("nombre") String nombre);
}