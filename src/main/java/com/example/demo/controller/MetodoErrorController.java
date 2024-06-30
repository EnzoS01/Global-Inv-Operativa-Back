package com.example.demo.controller;

import com.example.demo.entities.MetodoError;
import com.example.demo.services.MetodoErrorServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/metodosError")
public class MetodoErrorController extends BaseControllerImpl<MetodoError, MetodoErrorServiceImpl>{
}
