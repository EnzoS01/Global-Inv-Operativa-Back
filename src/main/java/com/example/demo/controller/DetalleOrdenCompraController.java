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


    @PostMapping("/nuevoDetalle")
    public ResponseEntity<?> nuevaOrdenCompra(@RequestBody DetalleOrdenCompra Detalle) {
        try {
            System.out.println("OC ID: " + Detalle.getId());

            System.out.println("OC total: " + Detalle.getSubtotal());
            System.out.println("OC ID: " + Detalle.getArticulo().getNombreArticulo());
            System.out.println("Detalle recibido: " + Detalle);
            DetalleOrdenCompra detalleAct = DetalleService.nuevoDetalle(Detalle);
            return ResponseEntity.status(HttpStatus.OK).body(detalleAct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    //te devuelve el detalle por defecto con el proveedor predeterminado para el articulo
    @PostMapping("/agregarAr/{detalleId}/{idArticulo}")
    public ResponseEntity<?> agregaArticuloDetalle(@PathVariable Long detalleId, @PathVariable Long idArticulo) {
        try {
            System.out.println("Detalle ID: " + detalleId + ", Artículo ID: " + idArticulo);
            DetalleOrdenCompra detalleActualizado = DetalleService.DetallePorDefecto(detalleId, idArticulo);
            return ResponseEntity.status(HttpStatus.OK).body(detalleActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    //te actualiza el detalle creado anteriormente con la info que vos elegis
    @PostMapping("/agregarPr/{detalleId}/{idProveedor}/{cantidad}")
    public ResponseEntity<?> agregaProveedorDetalle(@PathVariable Long detalleId, @PathVariable Long idProveedor, @PathVariable int cantidad) {
        try {
            System.out.println("Detalle ID: " + detalleId + ", proveedor ID: " + idProveedor + ", cantidad: "+ cantidad);
            DetalleOrdenCompra detalleActualizado = DetalleService.setearProveedor(detalleId, idProveedor, cantidad);
            return ResponseEntity.status(HttpStatus.OK).body(detalleActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
}
