package com.example.demo.repositories;

import com.example.demo.entities.Pronostico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PronosticoRepository extends BaseRepository<Pronostico, Long> {

    @Query("SELECT p FROM Pronostico p WHERE p.nombrePronostico = :nombrePronostico")
    List<Pronostico> findByNombrePronostico(@Param("nombrePronostico") String nombrePronostico);

    @Query("SELECT p FROM Pronostico p WHERE p.attribute = :value")
    List<Pronostico> findByAttribute(@Param("value") String value);
}
