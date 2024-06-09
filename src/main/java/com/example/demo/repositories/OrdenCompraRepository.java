package com.example.demo.repositories;

import com.example.demo.entities.OrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends BaseRepository<OrdenCompra,Long>{

    @Query(value="select * from ordenCompra o where o.fk_estadoOrdenCompra = :idEstado1 or o.fk_estadoOrdenCompra = :idEstado2", nativeQuery = true)
    List<OrdenCompra> findByState(Long idEstado1, Long idEstado2);

}
