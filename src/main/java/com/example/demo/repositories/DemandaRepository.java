package com.example.demo.repositories;

import java.util.List;

import com.example.demo.entities.Articulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Demanda;

@Repository
public interface DemandaRepository extends BaseRepository<Demanda,Long>{

    @Query(value="SELECT * FROM demanda D " +
                 "WHERE D.NUM_PERIODO >= :periodoDesde AND D.NUM_PERIODO <= :periodoHasta AND D.ANIO >= :anioDesde AND D.ANIO <= :anioHasta AND D.FK_ARTICULO= :idArticulo"
                 ,nativeQuery = true)
    List<Demanda> findByArticuloDesdeHasta(@Param("idArticulo") Long idArticulo,
                                           @Param("periodoDesde") int periodoDesde,
                                           @Param("anioDesde") int anioDesde,
                                           @Param("periodoHasta") int periodoHasta,
                                           @Param("anioHasta") int anioHasta);

   // @Query(value = "SELECT * FROM demanda D WHERE D.ANIO = :anio AND D.FK_ARTICULO = :idArticulo", nativeQuery = true)
   // List<Demanda> findByArticuloAnio(@Param("idArticulo") Long idArticulo, @Param("anio") int anio);

    List<Demanda> findByArticuloAndAnio(Articulo Articulo, int anio);

    //@Query(value = "SELECT * FROM demanda D " + "WHERE D.anio LIKE :anio AND D.fk_articulo = :idArticulo AND D.numPeriodo = :periodo",nativeQuery = true)
    //Demanda findByArticuloAnioPeriodo(@Param("idArticulo") Long idArticulo, @Param("anio") int anio,@Param("periodo")int periodo);

    Demanda findByArticuloAndAnioAndNumPeriodo(Articulo articulo, int anio, int numPeriodo);
}


