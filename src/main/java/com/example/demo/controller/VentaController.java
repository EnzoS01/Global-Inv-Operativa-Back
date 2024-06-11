package com.example.demo.controller;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Cliente;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Venta;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.services.VentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/ventas")
public class VentaController extends BaseControllerImpl<Venta, VentaServiceImpl> {

    @Autowired
    VentaServiceImpl ventaService;
    @Autowired
    ClienteRepository ClienteRepo;
    @Autowired
    ArticuloRepository ArticuloRepo;


    @PostMapping("/agregarDetalle/{ventaId}")
    //Con este metodo se agrega un detalleVenta a una venta existente, revisar VentaServiceImpl
    public ResponseEntity<?> agregarDetalleVenta(@PathVariable Long ventaId, @RequestBody DetalleVenta detalleVenta) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ventaService.agregarDetalleVenta(ventaId, detalleVenta));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularTotal/{ventaId}")
    public ResponseEntity<?> calcularTotal(@PathVariable Long ventaId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ventaService.calcularTotal(ventaId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/agregarCliente/{ventaId}/{clienteId}")
    public ResponseEntity<?> setCliente(@PathVariable Long ventaId,@PathVariable Long clienteId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ventaService.agregarCliente(ventaId,clienteId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

}
