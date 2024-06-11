package com.example.demo.controller;

import com.example.demo.entities.OrdenCompra;
import com.example.demo.services.OrdenCompraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
