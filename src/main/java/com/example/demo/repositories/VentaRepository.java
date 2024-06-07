package com.example.demo.repositories;

import com.example.demo.entities.Venta;
import com.example.demo.services.BaseService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentaRepository extends BaseRepository<Venta,Long> {

    @Query(value = "select * from venta V where V.fechaRealizacion >= :fecha1 and V.fechafechaRealizacion <= :fecha2", nativeQuery = true)
    List<Venta> findByFecha(@Param("fecha1") Date fecha1, @Param("fecha2") Date fecha2);

}
