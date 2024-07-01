package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Modelo;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DetalleVentaRepository;
import com.example.demo.repositories.ModeloRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleVentaServiceImpl extends BaseServiceImpl<DetalleVenta,Long> implements DetalleVentaService {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private OrdenCompraServiceImpl OrdenCompraService;
    @Autowired
    private ModeloRepository ModeloRepo;

    public DetalleVentaServiceImpl(BaseRepository<DetalleVenta, Long> baseRepository, DetalleVentaRepository detalleVentaRepository) {
        super(baseRepository);
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Transactional
    public DetalleVenta agregarArticulo(Long detalleVentaId, Long articuloId){
        Articulo articulo = articuloRepository.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("Articulo no encontrado"));

        DetalleVenta detalleVenta = detalleVentaRepository.findById(detalleVentaId)
                .orElseThrow(() -> new RuntimeException("DetalleVenta no encontrado"));

        // Validar que la cantidad actual del artículo sea suficiente
        if (articulo.getCantActual() < detalleVenta.getCantidad()) {
            throw new RuntimeException("Cantidad insuficiente del artículo");
        }

        articulo.setCantActual(articulo.getCantActual() - detalleVenta.getCantidad());
        detalleVenta.setSubtotal(articulo.getPrecioVenta() * detalleVenta.getCantidad());

        // Guardar los cambios en el artículo y el detalle de venta
        articuloRepository.save(articulo);
        detalleVenta.setArticulo(articulo);
        detalleVentaRepository.save(detalleVenta);
        System.out.println("ya actualice el detalle venta" + detalleVenta.getId() + "con el artiulo " + articulo.getNombreArticulo() );

        //En base al modelo del articulo verifico si pido o no
        System.out.println("Estoy por verificar si el producto necesita  una OC" );
        Modelo m = ModeloRepo.FindByNombre("LOTE_FIJO");
        try {
            if (articulo.getModelo().equals(m)) {
                if (articulo.getCantActual() <= articulo.getPuntoPedido()) {
                    // Crear orden de compra para lote fijo
                    System.out.println("Ahora estoy por crear el de lote fijo para : " + articulo.getNombreArticulo() );
                    OrdenCompra o = OrdenCompraService.OrdenStockFaltante(articulo.getId(), articulo.getLoteOptimo());
                    System.out.println("Se creó una orden de compra: " + o);
                }
            } else {
                // Si es intervalo fijo, verificar si existe una orden en curso
                if (articulo.getCantActual() <= articulo.getPuntoPedido()) {
                    if (!OrdenCompraService.existeOrden(articulo.getId())) {
                        // Calcular cantidad necesaria
                        System.out.println("Ahora estoy por crear el de intervalo fijo para : " + articulo.getNombreArticulo() );
                        int nivelMaximo = articulo.getLoteOptimo() + articulo.getPuntoPedido();
                        int cantidadNecesaria = nivelMaximo - articulo.getCantActual();

                        // Crear orden de compra para intervalo fijo
                        OrdenCompra o = OrdenCompraService.OrdenStockFaltante(articulo.getId(), cantidadNecesaria);
                        System.out.println("Se creó una orden de compra: " + o);
                    }
                }
            }
        } catch (Exception e) {

            System.err.println("Error al generar orden de compra: " + e.getMessage());
            e.printStackTrace();
        }

        return detalleVenta;
    }
}
