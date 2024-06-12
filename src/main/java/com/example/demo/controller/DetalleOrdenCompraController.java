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

    //te devuelve el detalle por defecto con el proveedor predeterminado para el articulo
    @PostMapping("/agregarAr/{detalleId}/{idArticulo}")
    public ResponseEntity<?> agregaArticuloDetalle(@PathVariable Long detalleId, @PathVariable Long idArticulo) {
        try {
            DetalleOrdenCompra detalleActualizado = DetalleService.DetallePorDefecto(detalleId, idArticulo);
            return ResponseEntity.status(HttpStatus.OK).body(detalleActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    //te actualiza el detalle creado anteriormente con la info que vos elegis
    @PostMapping("/agregarPr/{detalleId}/{idProveedor}")
    public ResponseEntity<?> agregaProveedorDetalle(@PathVariable Long detalleId, @PathVariable Long idProveedor, @RequestParam int cantidad) {
        try {
            DetalleOrdenCompra detalleActualizado = DetalleService.setearProveedor(detalleId, idProveedor, cantidad);
            return ResponseEntity.status(HttpStatus.OK).body(detalleActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
}
