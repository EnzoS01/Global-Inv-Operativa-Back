package com.example.demo.repositories;

import java.util.List;

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

    @Query(value = "SELECT * FROM demanda D WHERE D.anio LIKE %:anio% AND D.fk_articulo LIKE %:idArticulo% ",nativeQuery = true)
    List<Demanda> findByArticuloAnio(@Param("idArticulo") Long idArticulo, @Param("anio") int anio);

    @Query(value = "SELECT * FROM demanda D WHERE D.anio LIKE %:anio% AND D.fk_articulo LIKE %:idArticulo% AND D.numPeriodo LIKE %:periodo%",nativeQuery = true)
    Demanda findByArticuloAnioPeriodo(@Param("idArticulo") Long idArticulo, @Param("anio") int anio,@Param("periodo")int periodo);
}


