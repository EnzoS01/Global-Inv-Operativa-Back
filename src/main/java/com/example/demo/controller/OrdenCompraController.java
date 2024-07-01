package com.example.demo.controller;

import com.example.demo.DTO.DTOProveedorArticulo;
import com.example.demo.entities.DetalleOrdenCompra;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.services.OrdenCompraServiceImpl;
import lombok.Getter;
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

    @GetMapping("/ExisteOrden/{idArticulo}")
    public ResponseEntity<?> VerOrden(@PathVariable Long idArticulo){
        try{
            //ServicioOrdenCompra.existeOrden(idArticulo);
            System.out.println("Al controlador llego este id: " + idArticulo );
            return ResponseEntity.status(HttpStatus.OK).body(ServicioOrdenCompra.existeOrden(idArticulo));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/nuevaOrden")
    public ResponseEntity<?> nuevaOrdenCompra(@RequestBody OrdenCompra OrdenCompra) {
        try {
            System.out.println("OC ID: " + OrdenCompra.getId());
            System.out.println("OC estado: " + OrdenCompra.getEstadoOrdenCompra().getNombreEstado());
            System.out.println("OC total: " + OrdenCompra.getTotal());
            System.out.println("OC ID: " + OrdenCompra.getDetallesOrdenCompra());
            System.out.println("Detalle recibido: " + OrdenCompra);
            OrdenCompra ordenActualizada = ServicioOrdenCompra.crearUna(OrdenCompra);
            return ResponseEntity.status(HttpStatus.OK).body(ordenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/agregarDetalle/{ordenCompraId}")
    public ResponseEntity<?> agregarDetalleOrdenCompra(@PathVariable Long ordenCompraId, @RequestBody DetalleOrdenCompra detalleOrdenCompra) {
        try {
            System.out.println("OC ID: " + ordenCompraId);
            System.out.println("Detalle recibido: " + detalleOrdenCompra);
            OrdenCompra ordenActualizada = ServicioOrdenCompra.agregarDetalleOrdenCompra(ordenCompraId, detalleOrdenCompra);
            return ResponseEntity.status(HttpStatus.OK).body(ordenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @GetMapping("/datoscreacion")
    public ResponseEntity<?> DatosCreacion() {
        try {
            List<DTOProveedorArticulo> DTOProveedorArticulo = ServicioOrdenCompra.BuildDTOData();
            System.out.println("Se cre el DTO de los datos: " + DTOProveedorArticulo.size());



            return ResponseEntity.status(HttpStatus.OK).body(DTOProveedorArticulo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PutMapping("/finalizarOrden/{ordenCompraId}")
    public ResponseEntity<?> finalizarOC(@PathVariable Long ordenCompraId){
        try {
            System.out.println("OC ID: " + ordenCompraId);
            OrdenCompra ordenActualizada = ServicioOrdenCompra.finalizarCreacion(ordenCompraId);
            return ResponseEntity.status(HttpStatus.OK).body(ordenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PutMapping("/ActualizarEstado/{ordenCompraId}")
    public ResponseEntity<?> ActualizarEstado(@PathVariable Long ordenCompraId){
        try {
            System.out.println("OC ID: " + ordenCompraId);
            OrdenCompra ordenActualizada = ServicioOrdenCompra.ActualizarEstado(ordenCompraId);
            return ResponseEntity.status(HttpStatus.OK).body(ordenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

}
