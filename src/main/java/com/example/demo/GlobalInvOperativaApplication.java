package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.DemandaRepository;
import com.example.demo.repositories.EstadoOrdenCompraRepository;
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
	@Autowired
	private EstadoOrdenCompraRepository estadoOrdenCompraRepository;


	public static void main(String[] args) {
		SpringApplication.run(GlobalInvOperativaApplication.class, args);

	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			System.out.println("-------------------Funcionando------------------");
			Articulo art1= new Articulo();
			art1.setDetalle("Hamburguesa");
			art1.setCantActual(1000);
			art1.setLoteOptimo(500);
			art1.setPuntoPedido(980);
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

			Pronostico pron4= new Pronostico();

			pron4.setCantidadPeriodosHistoricos(3);
			pron4.setArticulo(art1);
			pron4.setNombrePronostico("Pronostico_Estacionalidad");
			pronosticoRepository.save(pron4);

			EstadoOrdenCompra estadoOrdenCompra= new EstadoOrdenCompra();
			estadoOrdenCompra.setNombreEstado("Pendiente");
			estadoOrdenCompraRepository.save(estadoOrdenCompra);


			//Demandas AÑO 2021
			Demanda demanda121=new Demanda();
			demanda121.setCantTotalDemanda(80);
			demanda121.setArticulo(art1);
			demanda121.setNumPeriodo(1);
			demanda121.setAnio(2021);

			Demanda demanda221=new Demanda();
			demanda221.setCantTotalDemanda(70);
			demanda221.setArticulo(art1);
			demanda221.setNumPeriodo(2);
			demanda221.setAnio(2021);

			Demanda demanda321=new Demanda();
			demanda321.setCantTotalDemanda(80);
			demanda321.setArticulo(art1);
			demanda321.setNumPeriodo(3);
			demanda321.setAnio(2021);

			Demanda demanda421=new Demanda();
			demanda421.setCantTotalDemanda(90);
			demanda421.setArticulo(art1);
			demanda421.setNumPeriodo(4);
			demanda421.setAnio(2021);

			Demanda demanda521=new Demanda();
			demanda521.setCantTotalDemanda(113);
			demanda521.setArticulo(art1);
			demanda521.setNumPeriodo(5);
			demanda521.setAnio(2021);

			Demanda demanda621=new Demanda();
			demanda621.setCantTotalDemanda(110);
			demanda621.setArticulo(art1);
			demanda621.setNumPeriodo(6);
			demanda621.setAnio(2021);

			Demanda demanda721=new Demanda();
			demanda721.setCantTotalDemanda(100);
			demanda721.setArticulo(art1);
			demanda721.setNumPeriodo(7);
			demanda721.setAnio(2021);

			Demanda demanda821=new Demanda();
			demanda821.setCantTotalDemanda(88);
			demanda821.setArticulo(art1);
			demanda821.setNumPeriodo(8);
			demanda821.setAnio(2021);

			Demanda demanda921=new Demanda();
			demanda921.setCantTotalDemanda(85);
			demanda921.setArticulo(art1);
			demanda921.setNumPeriodo(9);
			demanda921.setAnio(2021);

			Demanda demanda1021=new Demanda();
			demanda1021.setCantTotalDemanda(77);
			demanda1021.setArticulo(art1);
			demanda1021.setNumPeriodo(10);
			demanda1021.setAnio(2021);

			Demanda demanda1121=new Demanda();
			demanda1121.setCantTotalDemanda(75);
			demanda1121.setArticulo(art1);
			demanda1121.setNumPeriodo(11);
			demanda1121.setAnio(2021);

			Demanda demanda1221=new Demanda();
			demanda1221.setCantTotalDemanda(82);
			demanda1221.setArticulo(art1);
			demanda1221.setNumPeriodo(12);
			demanda1221.setAnio(2021);

			demandaRepository.save(demanda121);
			demandaRepository.save(demanda221);
			demandaRepository.save(demanda321);
			demandaRepository.save(demanda421);
			demandaRepository.save(demanda521);
			demandaRepository.save(demanda621);
			demandaRepository.save(demanda721);
			demandaRepository.save(demanda821);
			demandaRepository.save(demanda921);
			demandaRepository.save(demanda1021);
			demandaRepository.save(demanda1121);
			demandaRepository.save(demanda1221);

			//Demandas AÑO 2022
			Demanda demanda122=new Demanda();
			demanda122.setCantTotalDemanda(85);
			demanda122.setArticulo(art1);
			demanda122.setNumPeriodo(1);
			demanda122.setAnio(2022);

			Demanda demanda222=new Demanda();
			demanda222.setCantTotalDemanda(85);
			demanda222.setArticulo(art1);
			demanda222.setNumPeriodo(2);
			demanda222.setAnio(2022);

			Demanda demanda322=new Demanda();
			demanda322.setCantTotalDemanda(93);
			demanda322.setArticulo(art1);
			demanda322.setNumPeriodo(3);
			demanda322.setAnio(2022);

			Demanda demanda422=new Demanda();
			demanda422.setCantTotalDemanda(95);
			demanda422.setArticulo(art1);
			demanda422.setNumPeriodo(4);
			demanda422.setAnio(2022);

			Demanda demanda522=new Demanda();
			demanda522.setCantTotalDemanda(125);
			demanda522.setArticulo(art1);
			demanda522.setNumPeriodo(5);
			demanda522.setAnio(2022);

			Demanda demanda622=new Demanda();
			demanda622.setCantTotalDemanda(115);
			demanda622.setArticulo(art1);
			demanda622.setNumPeriodo(6);
			demanda622.setAnio(2022);

			Demanda demanda722=new Demanda();
			demanda722.setCantTotalDemanda(102);
			demanda722.setArticulo(art1);
			demanda722.setNumPeriodo(7);
			demanda722.setAnio(2022);

			Demanda demanda822=new Demanda();
			demanda822.setCantTotalDemanda(102);
			demanda822.setArticulo(art1);
			demanda822.setNumPeriodo(8);
			demanda822.setAnio(2022);

			Demanda demanda922=new Demanda();
			demanda922.setCantTotalDemanda(90);
			demanda922.setArticulo(art1);
			demanda922.setNumPeriodo(9);
			demanda922.setAnio(2022);

			Demanda demanda1022=new Demanda();
			demanda1022.setCantTotalDemanda(78);
			demanda1022.setArticulo(art1);
			demanda1022.setNumPeriodo(10);
			demanda1022.setAnio(2022);

			Demanda demanda1122=new Demanda();
			demanda1122.setCantTotalDemanda(82);
			demanda1122.setArticulo(art1);
			demanda1122.setNumPeriodo(11);
			demanda1122.setAnio(2022);

			Demanda demanda1222=new Demanda();
			demanda1222.setCantTotalDemanda(78);
			demanda1222.setArticulo(art1);
			demanda1222.setNumPeriodo(12);
			demanda1222.setAnio(2022);

			demandaRepository.save(demanda122);
			demandaRepository.save(demanda222);
			demandaRepository.save(demanda322);
			demandaRepository.save(demanda422);
			demandaRepository.save(demanda522);
			demandaRepository.save(demanda622);
			demandaRepository.save(demanda722);
			demandaRepository.save(demanda822);
			demandaRepository.save(demanda922);
			demandaRepository.save(demanda1022);
			demandaRepository.save(demanda1122);
			demandaRepository.save(demanda1222);

			//Demandas AÑO 2023
			Demanda demanda123=new Demanda();
			demanda123.setCantTotalDemanda(105);
			demanda123.setArticulo(art1);
			demanda123.setNumPeriodo(1);
			demanda123.setAnio(2023);

			Demanda demanda223=new Demanda();
			demanda223.setCantTotalDemanda(85);
			demanda223.setArticulo(art1);
			demanda223.setNumPeriodo(2);
			demanda223.setAnio(2023);

			Demanda demanda323=new Demanda();
			demanda323.setCantTotalDemanda(82);
			demanda323.setArticulo(art1);
			demanda323.setNumPeriodo(3);
			demanda323.setAnio(2023);

			Demanda demanda423=new Demanda();
			demanda423.setCantTotalDemanda(115);
			demanda423.setArticulo(art1);
			demanda423.setNumPeriodo(4);
			demanda423.setAnio(2023);

			Demanda demanda523=new Demanda();
			demanda523.setCantTotalDemanda(131);
			demanda523.setArticulo(art1);
			demanda523.setNumPeriodo(5);
			demanda523.setAnio(2023);

			Demanda demanda623=new Demanda();
			demanda623.setCantTotalDemanda(120);
			demanda623.setArticulo(art1);
			demanda623.setNumPeriodo(6);
			demanda623.setAnio(2023);

			Demanda demanda723=new Demanda();
			demanda723.setCantTotalDemanda(113);
			demanda723.setArticulo(art1);
			demanda723.setNumPeriodo(7);
			demanda723.setAnio(2023);

			Demanda demanda823=new Demanda();
			demanda823.setCantTotalDemanda(110);
			demanda823.setArticulo(art1);
			demanda823.setNumPeriodo(8);
			demanda823.setAnio(2023);

			Demanda demanda923=new Demanda();
			demanda923.setCantTotalDemanda(95);
			demanda923.setArticulo(art1);
			demanda923.setNumPeriodo(9);
			demanda923.setAnio(2023);

			Demanda demanda1023=new Demanda();
			demanda1023.setCantTotalDemanda(85);
			demanda1023.setArticulo(art1);
			demanda1023.setNumPeriodo(10);
			demanda1023.setAnio(2023);

			Demanda demanda1123=new Demanda();
			demanda1123.setCantTotalDemanda(83);
			demanda1123.setArticulo(art1);
			demanda1123.setNumPeriodo(11);
			demanda1123.setAnio(2023);

			Demanda demanda1223=new Demanda();
			demanda1223.setCantTotalDemanda(80);
			demanda1223.setArticulo(art1);
			demanda1223.setNumPeriodo(12);
			demanda1223.setAnio(2023);

			demandaRepository.save(demanda123);
			demandaRepository.save(demanda223);
			demandaRepository.save(demanda323);
			demandaRepository.save(demanda423);
			demandaRepository.save(demanda523);
			demandaRepository.save(demanda623);
			demandaRepository.save(demanda723);
			demandaRepository.save(demanda823);
			demandaRepository.save(demanda923);
			demandaRepository.save(demanda1023);
			demandaRepository.save(demanda1123);
			demandaRepository.save(demanda1223);



























		};

		}
}