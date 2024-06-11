package com.example.demo.repositories;

import com.example.demo.entities.Cliente;
import com.example.demo.entities.Venta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface ClienteRepository extends BaseRepository<Cliente,Long> {


    @Query(value = "select * from cliente C where C.id = :id", nativeQuery = true)
    List<Cliente> findByID(@Param("id") Long id);  // es REDUNDANTE YA HAY UNA FUNCION GENERICA QUE LO HACE

}
