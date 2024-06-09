package com.example.demo.repositories;

import com.example.demo.entities.EstadoOrdenCompra;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstadoOrdenCompraRepository extends BaseRepository<EstadoOrdenCompra,Long>{

    @Query(value="select * from estadoOrdenCompra E where E.nombreEstado like %:nombreEstado%", nativeQuery=true)
    EstadoOrdenCompra findByName(String nombreEstado);

    @Query(value="select * from estadoOrdenCompra E where E.fechaBaja is null", nativeQuery=true)
    List<EstadoOrdenCompra> findDisponibles();
}
