package com.example.demo.controller;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.DetalleVentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/detallesVenta")
public class DetalleVentaController extends BaseControllerImpl<DetalleVenta, DetalleVentaServiceImpl> {
    @Autowired
    private DetalleVentaServiceImpl detalleVentaService;

    @PostMapping("/agregarArticulo/{detalleVentaId}") //Con este metodo se agrega un articulo a una venta existente, revisar VentaServiceImpl
    public ResponseEntity<?> agregarDetalleVenta(@PathVariable Long detalleVentaId,@RequestBody Articulo articulo){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(detalleVentaService.agregarArticulo(detalleVentaId,articulo));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }


}
