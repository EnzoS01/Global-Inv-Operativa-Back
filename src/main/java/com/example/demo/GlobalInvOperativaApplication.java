package com.example.demo;

import com.example.demo.entities.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GlobalInvOperativaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlobalInvOperativaApplication.class, args);
		Articulo art = new Articulo();

		 Pronostico pronostico1 = new Pronostico();
		 pronostico1.setCantidadPeriodosHistoricos(3);
		 pronostico1.setArticulo(art);

		// System.out.println(pronostico1.getNumPronostico());

		// Pronostico pronostico2 = new Pronostico("PronosticoB", art);

		// System.out.println(pronostico2.getNumPronostico());

		List<Demanda> listaDemandas = new ArrayList<>();
		/*
		// Demanda para el año 2019
		 listaDemandas.add(new Demanda(1, 2019,200,art));
		 listaDemandas.add(new Demanda(2, 2019,art, 168));
		 listaDemandas.add(new Demanda(3, 2019,art, 159));
		 listaDemandas.add(new Demanda(4, 2019,art, 175));
		 listaDemandas.add(new Demanda(5, 2019,art, 190));
		 listaDemandas.add(new Demanda(6, 2019,art, 205));
		 listaDemandas.add(new Demanda(7, 2019,art, 180));
		 listaDemandas.add(new Demanda(8, 2019,art, 182));*/
	}

}
