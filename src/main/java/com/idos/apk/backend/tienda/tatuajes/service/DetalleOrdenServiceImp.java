package com.idos.apk.backend.tienda.tatuajes.service;

import com.idos.apk.backend.tienda.tatuajes.exceptions.OrdenNotFoundException;
import com.idos.apk.backend.tienda.tatuajes.exceptions.ProductoNotFoundException;
import com.idos.apk.backend.tienda.tatuajes.model.DetalleOrden;
import com.idos.apk.backend.tienda.tatuajes.model.Orden;
import com.idos.apk.backend.tienda.tatuajes.model.Producto;
import com.idos.apk.backend.tienda.tatuajes.model.dto.detalleorden.DetalleOrdenDto;
import com.idos.apk.backend.tienda.tatuajes.model.dto.detalleorden.DetalleOrdenDtoOne;
import com.idos.apk.backend.tienda.tatuajes.model.dto.producto.ProductoDTOOut;
import com.idos.apk.backend.tienda.tatuajes.repository.DetalleOrdenRepository;
import com.idos.apk.backend.tienda.tatuajes.repository.OrdenRepository;
import com.idos.apk.backend.tienda.tatuajes.repository.ProductoRepository;
import com.idos.apk.backend.tienda.tatuajes.service.interfaces.DetalleOrdenService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleOrdenServiceImp implements DetalleOrdenService {
    private final DetalleOrdenRepository repository;
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;

    public DetalleOrdenServiceImp(DetalleOrdenRepository repository, OrdenRepository ordenRepository, ProductoRepository productoRepository) {
        this.repository = repository;
        this.ordenRepository = ordenRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public DetalleOrdenDto save(DetalleOrdenDto objeto, String orden) throws ProductoNotFoundException {
        DetalleOrden nuevo = new DetalleOrden();
//        System.out.println(objeto.id());
        Producto producto = productoRepository.findById(objeto.id()).orElseThrow(() -> new ProductoNotFoundException("Produto no encontrado"));
        //Validacion de la entrada de cantidad y vacio de cantidad en el producto
        if (producto.getCantidad() - objeto.cantidad() == 0) {
            producto.setCantidad(0);
            producto.setEnable(false);
            productoRepository.save(producto);
        } else if (producto.getCantidad() - objeto.cantidad() < 0 || objeto.cantidad() < 0) {
            throw new ProductoNotFoundException("Cantidad erronea");
        } else {
            producto.setCantidad(producto.getCantidad() - objeto.cantidad());
            productoRepository.save(producto);
        }
        nuevo.setCantidad(objeto.cantidad());
        nuevo.setTotal(objeto.total());
        nuevo.setProducto(productoRepository.getReferenceById(objeto.id()));
        nuevo.setOrden(ordenRepository.getReferenceById(orden));
        repository.save(nuevo);
        return objeto;
    }

    @Override
    public DetalleOrdenDtoOne findOne(String id) throws OrdenNotFoundException {
        if (!repository.existsById(id)) {
            throw new OrdenNotFoundException("Detalle not found");
        } else {
            DetalleOrden orden = repository.findById(id).orElseThrow(() -> new OrdenNotFoundException("Detalle no encontrado"));
            Producto p = orden.getProducto();
            ProductoDTOOut enviar = new ProductoDTOOut(p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getTipo().getName(), p.getCantidad(), p.getImg());
            DetalleOrdenDtoOne detalle = new DetalleOrdenDtoOne(orden.getCantidad(), enviar, orden.getTotal());
            return detalle;
        }

    }

    @Override
    public List<DetalleOrdenDto> getAllByOrden(String num) throws OrdenNotFoundException {
        Orden orden = ordenRepository.findById(num).orElseThrow(() -> new OrdenNotFoundException("Orden no encontrada"));
        List<DetalleOrdenDto> enviar = repository.findAllByOrden_id(orden.getId()).stream().map(p -> mapper(p)).collect(Collectors.toList());
        return enviar;
    }

    private DetalleOrdenDto mapper(DetalleOrden detalleOrden) {
        DetalleOrdenDto enviar = new DetalleOrdenDto(detalleOrden.getCantidad(), detalleOrden.getProducto().getId(), detalleOrden.getTotal());
        return enviar;
    }
}
