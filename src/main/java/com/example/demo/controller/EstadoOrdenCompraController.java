package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.EstadoOrdenCompra;
import com.example.demo.services.DetalleVentaServiceImpl;
import com.example.demo.services.EstadoOrdenCompraServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/estadosOrdenCompra")
public class EstadoOrdenCompraController extends BaseControllerImpl<EstadoOrdenCompra, EstadoOrdenCompraServiceImpl>{
    @Autowired
    private EstadoOrdenCompraServiceImpl estadoOrdenCompraService;

}


