package com.example.demo.repositories;

import com.example.demo.entities.EstadoOrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstadoOrdenCompraRepository extends BaseRepository<EstadoOrdenCompra,Long>{

    @Query(value="select * from ESTADO_ORDEN_COMPRA E where E.NOMBRE_ESTADO  like %:nombreEstado%", nativeQuery=true)
    EstadoOrdenCompra findByName(@Param("nombreEstado") String nombreEstado);

    @Query(value="select * from estadoOrdenCompra E where E.fechaBaja is null", nativeQuery=true)
    List<EstadoOrdenCompra> findDisponibles();


}
