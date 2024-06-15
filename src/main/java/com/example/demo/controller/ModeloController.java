package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Modelo;
import com.example.demo.entities.Proveedor;
import com.example.demo.services.ModeloServiceImpl;
import com.example.demo.services.ProveedorServiceImpl;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/modelos")
public class ModeloController extends BaseControllerImpl<Modelo,ModeloServiceImpl>{

}
