package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.ProveedorArticulo;
import com.example.demo.services.ProveedorArticuloServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/proveedorArticulos")
public class ProveedorArticuloController extends BaseControllerImpl<ProveedorArticulo,ProveedorArticuloServiceImpl>{

}
