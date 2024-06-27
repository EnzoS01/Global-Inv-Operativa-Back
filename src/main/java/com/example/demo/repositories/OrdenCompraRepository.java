package com.example.demo.repositories;

import com.example.demo.entities.OrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends BaseRepository<OrdenCompra,Long>{

    @Query(value = "select * from ORDEN_COMPRA o where o.FK_ESTADO_ORDEN_COMPRA = :idEstado1 or o.FK_ESTADO_ORDEN_COMPRA = :idEstado2", nativeQuery = true)
    List<OrdenCompra> findByState(@Param("idEstado1") Long idEstado1, @Param("idEstado2") Long idEstado2);

    @Query(value = "select o.* from ordenCompra o join detalleOrdenCompra d on o.id = d.ordenCompra_id "+
                   "where d.articulo_id = :articuloId and (o.fk_estadoOrdenCompra = :idEstado1 or o.fk_estadoOrdenCompra = :idEstado2)", nativeQuery = true)
    List<OrdenCompra> findByArticuloAndEstado(@Param("articuloId") Long articuloId, @Param("idEstado1") Long idEstado1, @Param("idEstado2") Long idEstado2);

    @Query(value="select o from OrdenCompra o where o.estadoOrdenCompra.nombreEstado=?1")
    List<OrdenCompra> findByEstado(String estado);
}

