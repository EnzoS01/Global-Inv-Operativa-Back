package com.example.demo.repositories;

import com.example.demo.entities.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {


    @Query(value = "select * from articulo A where A.StockSeguridad>0 and A.CantActual>0 and A.fechaBaja is null", nativeQuery = true)
    List<Articulo> findAllDisponibles();                    //como se haria el manejo de cantActual y stock de seguridad??

    @Query(value = "select * from articulo A where A.CantActual<A.stockSeguridad", nativeQuery = true)
    List<Articulo> ListadoProductosFaltantes();

    @Query(value = "SELECT a.* FROM articulo a " +
                   "WHERE a.CantActual < a.puntoPedido " +
                   "AND NOT EXISTS (" +
                   "    SELECT 1 FROM detalleOrdenCompra doc JOIN ordenCompra oc ON doc.id = oc.id JOIN estadoOrdenCompra eoc ON oc.id = eoc.id " +
                   "    WHERE doc.id = a.id AND eoc.nombreEstado = 'Pendiente'", 
                   nativeQuery = true)
    List<Articulo> ListadoProductosAReponer();
}
