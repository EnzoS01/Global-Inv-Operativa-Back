package com.example.demo.controller;

import com.example.demo.entities.DetalleOrdenCompra;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.services.OrdenCompraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/ordenesCompra")
public class OrdenCompraController extends BaseControllerImpl<OrdenCompra, OrdenCompraServiceImpl>{

    @Autowired
    OrdenCompraServiceImpl ServicioOrdenCompra;
    
    @GetMapping("/VerTodas")//los metodos
    public ResponseEntity<?> VerTodas(){
        try{
            List<OrdenCompra> ordenes = ServicioOrdenCompra.ListaOrdenes();
            return ResponseEntity.status(HttpStatus.OK).body(ordenes);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/agregarDetalle/{ordenCompraId}")
    public ResponseEntity<?> agregarDetalleOrdenCompra(@PathVariable Long ordenCompraId, @RequestBody DetalleOrdenCompra detalleOrdenCompra) {
        try {
            OrdenCompra ordenActualizada = ServicioOrdenCompra.agregarDetalleOrdenCompra(ordenCompraId, detalleOrdenCompra);
            return ResponseEntity.status(HttpStatus.OK).body(ordenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PutMapping("/finalizarOrden/{ordenCompraId}")
    public ResponseEntity<?> finalizarOC(@PathVariable Long ordenCompraId){
        try {
            OrdenCompra ordenActualizada = ServicioOrdenCompra.finalizarCreacion(ordenCompraId);
            return ResponseEntity.status(HttpStatus.OK).body(ordenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PutMapping("/ActualizarEstado/{ordenCompraId}")
    public ResponseEntity<?> ActualizarEstado(@PathVariable Long ordenCompraId){
        try {
            OrdenCompra ordenActualizada = ServicioOrdenCompra.ActualizarEstado(ordenCompraId);
            return ResponseEntity.status(HttpStatus.OK).body(ordenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }

    }


}
