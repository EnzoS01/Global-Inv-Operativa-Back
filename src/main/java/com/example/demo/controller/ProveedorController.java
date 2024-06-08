package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Proveedor;
import com.example.demo.entities.ProveedorArticulo;
import com.example.demo.services.ProveedorArticuloServiceImpl;
import com.example.demo.services.ProveedorServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/proveedores")
public class ProveedorController extends BaseControllerImpl<Proveedor,ProveedorServiceImpl>{

}
