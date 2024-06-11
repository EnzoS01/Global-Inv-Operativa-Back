package com.example.demo.controller;

import com.example.demo.entities.DetalleOrdenCompra;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.services.DetalleOrdenCompraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/detalleOrden")
public class DetalleOrdenCompraController extends BaseControllerImpl<DetalleOrdenCompra, DetalleOrdenCompraServiceImpl>{

    @Autowired
    private DetalleOrdenCompraServiceImpl DetalleService;

    @PostMapping("/agregarAr/{detalleId}/{idArticulo}")
    public ResponseEntity<?> agregaArticuloDetalle(@PathVariable Long detalleId, @PathVariable Long idArticulo) {
        try {
            DetalleOrdenCompra detalleActualizado = DetalleService.setearArticulo(detalleId, idArticulo);
            return ResponseEntity.status(HttpStatus.OK).body(detalleActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/agregarPr/{detalleId}/{idProveedor}")
    public ResponseEntity<?> agregaProveedorDetalle(@PathVariable Long detalleId, @PathVariable Long idProveedor) {
        try {
            DetalleOrdenCompra detalleActualizado = DetalleService.setearArticulo(detalleId, idProveedor);
            return ResponseEntity.status(HttpStatus.OK).body(detalleActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
}
