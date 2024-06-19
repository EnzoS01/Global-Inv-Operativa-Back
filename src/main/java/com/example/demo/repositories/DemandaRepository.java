package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Demanda;

@Repository
public interface DemandaRepository extends BaseRepository<Demanda,Long>{


        @Query(value="SELECT * FROM demanda D WHERE D.NUM_PERIODO >= :periodoDesde AND D.NUM_PERIODO <= :periodoHasta AND D.ANIO >= :anioDesde AND D.ANIO <= :anioHasta", nativeQuery = true)
    List<Demanda> findByDesdeHasta(@Param("periodoDesde") int periodoDesde,
                                   @Param("anioDesde") int anioDesde,
                                   @Param("periodoHasta") int periodoHasta,
                                   @Param("anioHasta") int anioHasta);
}


