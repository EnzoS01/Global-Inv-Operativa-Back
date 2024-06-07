package com.example.demo.controller;

import com.example.demo.entities.Modelo;
import com.example.demo.services.ModeloServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/modelos")
public class ModeloController extends BaseControllerImpl<Modelo, ModeloServiceImpl> {
}
