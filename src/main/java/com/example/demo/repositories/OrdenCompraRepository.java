package com.example.demo.repositories;

import com.example.demo.entities.OrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends BaseRepository<OrdenCompra,Long>{

    @Query(value="select * from ordenCompra o where o.fk_estadoOrdenCompra = :idEstado1 or o.fk_estadoOrdenCompra = :idEstado2", nativeQuery = true)
    List<OrdenCompra> findByState(@Param("idEstado1") Long idEstado1, @Param("idEstado2") Long idEstado2);

    @Query(value = "select * from ordenCompra o join detalleOrdenCompra d on o.id = d.ordenCompra_id where d.articulo_id = :articuloId and (o.estadoOrdenCompra.nombreEstado = :estado1 or o.estadoOrdenCompra.nombreEstado = :estado2)", nativeQuery = true)
    List<OrdenCompra> findByArticuloAndEstado(@Param("articuloId") Long articuloId, @Param("idEstado1") String estado1, @Param("idEstado2") String estado2);

}
