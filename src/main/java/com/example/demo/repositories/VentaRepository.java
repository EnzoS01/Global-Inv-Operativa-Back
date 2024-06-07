package com.example.demo.repositories;

import com.example.demo.entities.Venta;
import com.example.demo.services.BaseService;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends BaseRepository<Venta,Long> {
}
