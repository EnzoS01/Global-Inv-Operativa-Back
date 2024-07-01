package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;

import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class GlobalInvOperativaApplication {
	@Autowired
	private ArticuloRepository articuloRepository;

	@Autowired
	private ModeloRepository modeloRepository;

	@Autowired
	private PronosticoRepository pronosticoRepository;

	@Autowired
	private DemandaRepository demandaRepository;
	@Autowired
	private EstadoOrdenCompraRepository estadoOrdenCompraRepository;

	@Autowired
	private OrdenCompraRepository ordenCompraRepository;

	@Autowired
	private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

	@Autowired
	private VentaRepository ventaRepository;
	@Autowired
	private DetalleVentaRepository detalleVentaRepository;
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ProveedorRepository ProveedorRepo;

	@Autowired
	private ProveedorArticuloRepository ProveedorArticuloRepo;


	public static void main(String[] args) {
		SpringApplication.run(GlobalInvOperativaApplication.class, args);

	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			System.out.println("----------------" +
					"---Funcionando------------------");


			//Creacion Cliente
			Cliente clie1=new Cliente();
			clie1.setNombre("Gerardo ");
			clie1.setDni(731732L);
			clienteRepository.save(clie1);

			Cliente clie2=new Cliente();
			clie2.setNombre("Mariano ");
			clie2.setDni(23445677L);
			clienteRepository.save(clie2);

			Cliente clie3=new Cliente();
			clie3.setNombre("Mariana");
			clie3.setDni(34599866L);
			clienteRepository.save(clie3);

			Cliente clie4=new Cliente();
			clie4.setNombre("Liliana");
			clie4.setDni(44788566L);
			clienteRepository.save(clie4);



			//PROVEEDORES
			LocalDate fechalocal = LocalDate.of(2024, 1, 4);

			// Convertir LocalDate a Date
			Date fecha = Date.from(fechalocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

			Proveedor proveedor1 = new Proveedor();
			proveedor1.setDireccion("Rodriguez 324");
			proveedor1.setMail("mayorista@gmail.com");
			proveedor1.setFechaAlta(fecha);
			proveedor1.setNombre("Mayorista");
			proveedor1.setTelefono(261789341);
			ProveedorRepo.save(proveedor1);

			Proveedor proveedor2 = new Proveedor();
			proveedor2.setDireccion("Pascal 123");
			proveedor2.setFechaAlta(fecha);
			proveedor2.setMail("Meraki@gmail.com");
			proveedor2.setNombre("Meraki");
			proveedor2.setTelefono(261789342);
			ProveedorRepo.save(proveedor2);

			Proveedor proveedor3 = new Proveedor();
			proveedor3.setDireccion("Ironia 324");
			proveedor3.setFechaAlta(fecha);
			proveedor3.setMail("Sakura@gmail.com");
			proveedor3.setNombre("Sakura");
			proveedor3.setTelefono(261789345);
			ProveedorRepo.save(proveedor3);

			//Creacion ModelosPrediccion
			ModeloPrediccion promedioPonderado=new ModeloPrediccion();
			promedioPonderado.setNombreModelo("Promedio_Ponderado");

			ModeloPrediccion promedioMovilSuavizado=new ModeloPrediccion();
			promedioMovilSuavizado.setNombreModelo("PM_Suavizado");

			ModeloPrediccion regresionLineal=new ModeloPrediccion();
			regresionLineal.setNombreModelo("Regresion_Lineal");

			ModeloPrediccion promedioEstacionalidad=new ModeloPrediccion();
			promedioEstacionalidad.setNombreModelo("Pronostico_Estacionalidad");


			//CREACION MODELOS
			Modelo loteFijo= new Modelo();
			loteFijo.setNombreModelo("LOTE_FIJO");
			modeloRepository.save(loteFijo);
			Modelo intervaloFijo= new Modelo();
			intervaloFijo.setNombreModelo("INTERVALO_FIJO");
			modeloRepository.save(intervaloFijo);

			//creacion ESTADO ORDENES DE COMPRA
			EstadoOrdenCompra pendiente = new EstadoOrdenCompra();
			pendiente.setNombreEstado("Pendiente");
			estadoOrdenCompraRepository.save(pendiente);

			EstadoOrdenCompra recibido = new EstadoOrdenCompra();
			recibido.setNombreEstado("Recibido");
			estadoOrdenCompraRepository.save(recibido);

			EstadoOrdenCompra enviada= new EstadoOrdenCompra();
			enviada.setNombreEstado("Enviada");
			estadoOrdenCompraRepository.save(enviada);

			EstadoOrdenCompra entregada= new EstadoOrdenCompra();
			entregada.setNombreEstado("Entregada");
			estadoOrdenCompraRepository.save(entregada);

			//CREACION ARTICULOS
			Articulo art1= new Articulo();
			art1.setNombreArticulo("Hamburguesa");
			art1.setDetalle("queso,jamon,huevo");
			art1.setCantActual(1000);
			art1.setLoteOptimo(500);
			art1.setPuntoPedido(980);
			art1.setPrecioVenta(2000);
			art1.setModelo(loteFijo);
			art1.setProveedorPredeterminado(proveedor1);
			articuloRepository.save(art1);

			Articulo pizza= new Articulo();
			pizza.setNombreArticulo("Pizza");
			pizza.setDetalle("queso,jamon,huevo,queso");
			pizza.setCantActual(1000);
			pizza.setLoteOptimo(500);
			pizza.setPuntoPedido(980);
			pizza.setStockSeguridad(1050);
			pizza.setPrecioVenta(4000);
			pizza.setModelo(intervaloFijo);
			pizza.setProveedorPredeterminado(proveedor3);
			articuloRepository.save(pizza);

			Articulo fideos= new Articulo();
			fideos.setNombreArticulo("Fideos con crema");
			fideos.setDetalle("fideos, crema, jamon y queso");
			fideos.setCantActual(1000);
			fideos.setLoteOptimo(500);
			fideos.setPuntoPedido(980);
			fideos.setStockSeguridad(1050);
			fideos.setPrecioVenta(2000);
			fideos.setModelo(intervaloFijo);
			fideos.setProveedorPredeterminado(proveedor2);
			articuloRepository.save(fideos);

			Articulo papasGrandesConCheddarYBacon= new Articulo();
			papasGrandesConCheddarYBacon.setNombreArticulo("Papas con cheddar y bacon");
			papasGrandesConCheddarYBacon.setDetalle("Papas con cheddar y pedazos de bacon por arriba");
			papasGrandesConCheddarYBacon.setCantActual(930);
			papasGrandesConCheddarYBacon.setLoteOptimo(500);
			papasGrandesConCheddarYBacon.setPuntoPedido(980);
			papasGrandesConCheddarYBacon.setStockSeguridad(1050);
			papasGrandesConCheddarYBacon.setModelo(intervaloFijo);
			papasGrandesConCheddarYBacon.setPrecioVenta(3000);
			papasGrandesConCheddarYBacon.setProveedorPredeterminado(proveedor1);
			articuloRepository.save(papasGrandesConCheddarYBacon);

			Articulo Empanadas= new Articulo();
			Empanadas.setNombreArticulo("Empanadas");
			Empanadas.setDetalle("Empanadas al horno");
			Empanadas.setCantActual(930);
			Empanadas.setLoteOptimo(500);
			Empanadas.setPuntoPedido(980);
			Empanadas.setStockSeguridad(1050);
			Empanadas.setPrecioVenta(2000);
			Empanadas.setModelo(loteFijo);
			Empanadas.setProveedorPredeterminado(proveedor2);
			articuloRepository.save(Empanadas);


			//ORDENES DE COMPRA
			OrdenCompra oc1 = new OrdenCompra();
			oc1.setEstadoOrdenCompra(recibido);
				DetalleOrdenCompra doc1= new DetalleOrdenCompra();
				doc1.setArticulo(papasGrandesConCheddarYBacon);
				doc1.setCantidad(2);
				doc1.setSubtotal(40);
				doc1.setLinea(1);
				detalleOrdenCompraRepository.save(doc1);
				DetalleOrdenCompra doc2= new DetalleOrdenCompra();
				doc2.setArticulo(Empanadas);
				doc2.setCantidad(2);
				doc2.setSubtotal(40);
				doc2.setLinea(2);
				detalleOrdenCompraRepository.save(doc2);
				oc1.addDetallesOrdenCompra(doc1);
				oc1.addDetallesOrdenCompra(doc2);
			ordenCompraRepository.save(oc1);

			OrdenCompra oc2 = new OrdenCompra();
			oc2.setEstadoOrdenCompra(pendiente);
				DetalleOrdenCompra doc3= new DetalleOrdenCompra();
				doc3.setArticulo(papasGrandesConCheddarYBacon);
				doc3.setCantidad(2);
				doc3.setSubtotal(90);
				doc3.setLinea(1);
				detalleOrdenCompraRepository.save(doc3);
				DetalleOrdenCompra doc4= new DetalleOrdenCompra();
				doc4.setArticulo(Empanadas);
				doc4.setCantidad(2);
				doc4.setSubtotal(200);
				doc4.setLinea(2);
				detalleOrdenCompraRepository.save(doc4);
				oc2.addDetallesOrdenCompra(doc3);
				oc2.addDetallesOrdenCompra(doc4);
				oc2.setTotal(doc3.getSubtotal() + doc4.getSubtotal());
			ordenCompraRepository.save(oc2);

			OrdenCompra oc3 = new OrdenCompra();
			oc3.setEstadoOrdenCompra(enviada);
				DetalleOrdenCompra doc5= new DetalleOrdenCompra();
				doc5.setArticulo(papasGrandesConCheddarYBacon);
				doc5.setCantidad(2);
				doc5.setSubtotal(100);
				doc5.setLinea(1);
				detalleOrdenCompraRepository.save(doc5);
				DetalleOrdenCompra doc6= new DetalleOrdenCompra();
				doc6.setArticulo(Empanadas);
				doc6.setCantidad(2);
				doc6.setSubtotal(789);
				doc6.setLinea(2);
				detalleOrdenCompraRepository.save(doc6);
				oc2.addDetallesOrdenCompra(doc5);
				oc2.addDetallesOrdenCompra(doc6);
				oc3.setTotal(doc5.getSubtotal() + doc6.getSubtotal());
			ordenCompraRepository.save(oc3);


			//PROVEEDORARTICULO
			ProveedorArticulo PA1 = new ProveedorArticulo();
			PA1.setArticulo(pizza);
			PA1.setFechaAlta(LocalDate.now());
			PA1.setProveedor(proveedor1);
			PA1.setCostoPedido(254);
			PA1.setCostoAlmacenamiento(354);
			PA1.setCostoProducto(654);
			PA1.setTiempoPedido(4);
			ProveedorArticuloRepo.save(PA1);

			ProveedorArticulo PA2 = new ProveedorArticulo();
			PA2.setArticulo(pizza);
			PA2.setFechaAlta(LocalDate.now());
			PA2.setProveedor(proveedor2);
			PA2.setCostoPedido(345);
			PA2.setCostoAlmacenamiento(567);
			PA2.setCostoProducto(789);
			PA2.setTiempoPedido(20);
			ProveedorArticuloRepo.save(PA2);

			ProveedorArticulo PA3 = new ProveedorArticulo();
			PA3.setArticulo(pizza);
			PA3.setFechaAlta(LocalDate.now());
			PA3.setProveedor(proveedor3);
			PA3.setCostoPedido(469);
			PA3.setCostoAlmacenamiento(800);
			PA3.setCostoProducto(789);
			PA3.setTiempoPedido(28);
			ProveedorArticuloRepo.save(PA3);

			ProveedorArticulo PA4 = new ProveedorArticulo();
			PA4.setArticulo(Empanadas);
			PA4.setFechaAlta(LocalDate.now());
			PA4.setProveedor(proveedor1);
			PA4.setCostoPedido(254);
			PA4.setCostoAlmacenamiento(354);
			PA4.setCostoProducto(654);
			PA4.setTiempoPedido(4);
			ProveedorArticuloRepo.save(PA4);

			ProveedorArticulo PA5 = new ProveedorArticulo();
			PA5.setArticulo(Empanadas);
			PA5.setFechaAlta(LocalDate.now());
			PA5.setProveedor(proveedor2);
			PA5.setCostoPedido(345);
			PA5.setCostoAlmacenamiento(567);
			PA5.setCostoProducto(789);
			PA5.setTiempoPedido(20);
			ProveedorArticuloRepo.save(PA5);

			ProveedorArticulo PA6 = new ProveedorArticulo();
			PA6.setArticulo(Empanadas);
			PA6.setFechaAlta(LocalDate.now());
			PA6.setProveedor(proveedor3);
			PA6.setCostoPedido(469);
			PA6.setCostoAlmacenamiento(800);
			PA6.setCostoProducto(789);
			PA6.setTiempoPedido(28);
			ProveedorArticuloRepo.save(PA6);

			ProveedorArticulo PA7 = new ProveedorArticulo();
			PA7.setArticulo(fideos);
			PA7.setFechaAlta(LocalDate.now());
			PA7.setProveedor(proveedor1);
			PA7.setCostoPedido(254);
			PA7.setCostoAlmacenamiento(354);
			PA7.setCostoProducto(654);
			PA7.setTiempoPedido(4);
			ProveedorArticuloRepo.save(PA7);

			ProveedorArticulo PA8 = new ProveedorArticulo();
			PA8.setArticulo(fideos);
			PA8.setFechaAlta(LocalDate.now());
			PA8.setProveedor(proveedor2);
			PA8.setCostoPedido(345);
			PA8.setCostoAlmacenamiento(567);
			PA8.setCostoProducto(789);
			PA8.setTiempoPedido(20);
			ProveedorArticuloRepo.save(PA8);

			ProveedorArticulo PA9 = new ProveedorArticulo();
			PA9.setArticulo(fideos);
			PA9.setFechaAlta(LocalDate.now());
			PA9.setProveedor(proveedor3);
			PA9.setCostoPedido(469);
			PA9.setCostoAlmacenamiento(800);
			PA9.setCostoProducto(789);
			PA9.setTiempoPedido(28);
			ProveedorArticuloRepo.save(PA9);

			ProveedorArticulo PA10 = new ProveedorArticulo();
			PA10.setArticulo(art1);
			PA10.setFechaAlta(LocalDate.now());
			PA10.setProveedor(proveedor1);
			PA10.setCostoPedido(254);
			PA10.setCostoAlmacenamiento(354);
			PA10.setCostoProducto(654);
			PA10.setTiempoPedido(4);
			ProveedorArticuloRepo.save(PA10);

			ProveedorArticulo PA11 = new ProveedorArticulo();
			PA11.setArticulo(art1);
			PA11.setFechaAlta(LocalDate.now());
			PA11.setProveedor(proveedor2);
			PA11.setCostoPedido(345);
			PA11.setCostoAlmacenamiento(567);
			PA11.setCostoProducto(789);
			PA11.setTiempoPedido(20);
			ProveedorArticuloRepo.save(PA11);

			ProveedorArticulo PA12 = new ProveedorArticulo();
			PA12.setArticulo(art1);
			PA12.setFechaAlta(LocalDate.now());
			PA12.setProveedor(proveedor3);
			PA12.setCostoPedido(469);
			PA12.setCostoAlmacenamiento(800);
			PA12.setCostoProducto(789);
			PA12.setTiempoPedido(28);
			ProveedorArticuloRepo.save(PA12);

			ProveedorArticulo PA13 = new ProveedorArticulo();
			PA13.setArticulo(papasGrandesConCheddarYBacon);
			PA13.setFechaAlta(LocalDate.now());
			PA13.setProveedor(proveedor3);
			PA13.setCostoPedido(469);
			PA13.setCostoAlmacenamiento(800);
			PA13.setCostoProducto(789);
			PA13.setTiempoPedido(28);
			ProveedorArticuloRepo.save(PA13);



			//Creacion Pronostico
			Pronostico pron1= new Pronostico();
			pron1.setCantidadPeriodosHistoricos(4);
			pron1.setAnioAPredecir(2023);
			pron1.setPeriodoAPredecir(2);
			pron1.setArticulo(art1);
			pronosticoRepository.save(pron1);



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


			//VENTAS
			Venta venta=new Venta();
			LocalDate localDate = LocalDate.of(2024, 1, 4);

			// Convertir LocalDate a Date
			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			venta.setCliente(clie1);
			venta.setFechaRealizacion(date);

			DetalleVenta detallev1=new DetalleVenta();
			detallev1.setArticulo(art1);
			detallev1.setCantidad(20);
			venta.addDetallesVenta(detallev1);
			detalleVentaRepository.save(detallev1);

			ventaRepository.save(venta);

























		};

		}
}