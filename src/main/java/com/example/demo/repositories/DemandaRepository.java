package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Demanda;

@Repository
public interface DemandaRepository extends BaseRepository<Demanda,Long>{

        @Query(value="select * from demanda d where d.numPeriodo>=periododesde and  d.numPeriodo<=periodohasta and d.año>=añodesde and d.año<=añohasta", nativeQuery = true)
        List<Demanda> findByDesdeHasta(int periododesde,int añodesde, int periodohasta, int añohasta);

}
