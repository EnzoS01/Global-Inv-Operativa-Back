package com.example.demo.controller;

import com.example.demo.entities.ModeloPrediccion;
import com.example.demo.services.ModeloPrediccionServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/modelosPrediccion")
public class ModeloPrediccionController extends BaseControllerImpl<ModeloPrediccion, ModeloPrediccionServiceImpl>{
}
