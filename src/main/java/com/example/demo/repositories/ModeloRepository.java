package com.example.demo.repositories;

import com.example.demo.entities.Modelo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModeloRepository extends BaseRepository<Modelo,Long>{

    @Query(value= "select * from MODELO m where m.NOMBRE_MODELO = :nombre", nativeQuery=true)
    Modelo FindByNombre(@Param("nombre") String nombre);

}
