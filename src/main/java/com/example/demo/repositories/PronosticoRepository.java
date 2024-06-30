package com.example.demo.repositories;

import com.example.demo.entities.Pronostico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PronosticoRepository extends BaseRepository<Pronostico, Long> {


    @Query(
            value = "SELECT p FROM Pronostico p WHETE p.articulo_id LIKE %:articulo_id%",
            nativeQuery = true)
    List<Pronostico> findByArticulo(@Param("articulo_id")Long articulo_id);
    
}
