package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.DemandaRepository;
import com.example.demo.repositories.PronosticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GlobalInvOperativaApplication {
	@Autowired
	private ArticuloRepository articuloRepository;

	@Autowired
	private PronosticoRepository pronosticoRepository;

	@Autowired
	private DemandaRepository demandaRepository;


	public static void main(String[] args) {
		SpringApplication.run(GlobalInvOperativaApplication.class, args);

	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			System.out.println("-------------------Funcionando------------------");
			Articulo art1= new Articulo();
			art1.setDetalle("Hamburguesa");
			articuloRepository.save(art1);

			Pronostico pron1= new Pronostico();

			pron1.setCantidadPeriodosHistoricos(3);
			pron1.setArticulo(art1);
			pron1.setNombrePronostico("Promedio_Ponderado");
			pronosticoRepository.save(pron1);

			Pronostico pron2= new Pronostico();

			pron2.setCantidadPeriodosHistoricos(3);
			pron2.setArticulo(art1);
			pron2.setNombrePronostico("PM_Suavizad0");
			pronosticoRepository.save(pron2);

			Pronostico pron3= new Pronostico();

			pron3.setCantidadPeriodosHistoricos(3);
			pron3.setArticulo(art1);
			pron3.setNombrePronostico("Regresion_Lineal");
			pronosticoRepository.save(pron3);

			Demanda demanda123=new Demanda();
			demanda123.setCantTotalDemanda(100);
			demanda123.setArticulo(art1);
			demanda123.setNumPeriodo(1);
			demanda123.setAnio(2023);

			Demanda demanda223=new Demanda();
			demanda223.setCantTotalDemanda(93);
			demanda223.setArticulo(art1);
			demanda223.setNumPeriodo(2);
			demanda223.setAnio(2023);

			Demanda demanda323=new Demanda();
			demanda323.setCantTotalDemanda(81);
			demanda323.setArticulo(art1);
			demanda323.setNumPeriodo(3);
			demanda323.setAnio(2023);

			Demanda demanda423=new Demanda();
			demanda423.setCantTotalDemanda(102);
			demanda423.setArticulo(art1);
			demanda423.setNumPeriodo(4);
			demanda423.setAnio(2023);

			Demanda demanda523=new Demanda();
			demanda523.setCantTotalDemanda(89);
			demanda523.setArticulo(art1);
			demanda523.setNumPeriodo(5);
			demanda523.setAnio(2023);

			Demanda demanda623=new Demanda();
			demanda623.setCantTotalDemanda(97);
			demanda623.setArticulo(art1);
			demanda623.setNumPeriodo(6);
			demanda623.setAnio(2023);

			Demanda demanda723=new Demanda();
			demanda723.setCantTotalDemanda(98);
			demanda723.setArticulo(art1);
			demanda723.setNumPeriodo(7);
			demanda723.setAnio(2023);

			Demanda demanda823=new Demanda();
			demanda823.setCantTotalDemanda(92);
			demanda823.setArticulo(art1);
			demanda823.setNumPeriodo(8);
			demanda823.setAnio(2023);

			Demanda demanda923=new Demanda();
			demanda923.setCantTotalDemanda(88);
			demanda923.setArticulo(art1);
			demanda923.setNumPeriodo(9);
			demanda923.setAnio(2023);

			Demanda demanda1023=new Demanda();
			demanda1023.setCantTotalDemanda(87);
			demanda1023.setArticulo(art1);
			demanda1023.setNumPeriodo(10);
			demanda1023.setAnio(2023);

			Demanda demanda1123=new Demanda();
			demanda1123.setCantTotalDemanda(89);
			demanda1123.setArticulo(art1);
			demanda1123.setNumPeriodo(11);
			demanda1123.setAnio(2023);

			Demanda demanda1223=new Demanda();
			demanda1223.setCantTotalDemanda(97);
			demanda1223.setArticulo(art1);
			demanda1223.setNumPeriodo(12);
			demanda1223.setAnio(2023);

			demandaRepository.save(demanda123);
			demandaRepository.save(demanda223);
			demandaRepository.save(demanda323);
			demandaRepository.save(demanda423);
			//demandaRepository.save(demanda523);




























		};

		}
}