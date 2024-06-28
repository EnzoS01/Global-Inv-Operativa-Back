package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Proveedor;
import com.example.demo.entities.ProveedorArticulo;
import com.example.demo.services.ArticuloService;
import com.example.demo.services.ProveedorArticuloServiceImpl;
import com.example.demo.services.ProveedorService;
import com.example.demo.services.ProveedorServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/proveedores")
public class ProveedorController extends BaseControllerImpl<Proveedor,ProveedorServiceImpl>{
    @Autowired
    protected ProveedorService proveedorService;

    @GetMapping("/getProveedoresQueProveenUnArticulo/{idArticulo}")
    public ResponseEntity<?> getProveedoresQueProveenUnArticulo(@PathVariable Long idArticulo){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(proveedorService.ObtenerProveedoresQueProveenUnArticulo(idArticulo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

}
