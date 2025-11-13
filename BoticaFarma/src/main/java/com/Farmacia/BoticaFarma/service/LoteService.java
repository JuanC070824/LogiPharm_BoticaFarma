package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.model.*;
import com.Farmacia.BoticaFarma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoteService {

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CompraRepository compraRepository;

    // Crear lote (agregar stock)
    @Transactional
    public Lote crearLote(Integer idProducto, Integer idUsuario, Integer cantidad,
                          LocalDate fechaVencimiento, BigDecimal precioCompra) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // CREAR COMPRA AUTOMÁTICAMENTE
        BigDecimal totalCompra = precioCompra.multiply(new BigDecimal(cantidad));
        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setAlmacen(null);  // Lo vas a borrar según dijiste
        compra.setFecha(LocalDateTime.now());
        compra.setValor(totalCompra);

        Compra compraGuardada = compraRepository.save(compra);

        // Ahora crear el lote con la compra asociada
        String codigoLote = generarCodigoLote(producto);
        EstadoLote estadoInicial = calcularEstadoLote(cantidad, cantidad, fechaVencimiento);

        Lote lote = new Lote();
        lote.setProducto(producto);
        lote.setCompra(compraGuardada);
        lote.setCodigoLote(codigoLote);
        lote.setCantidadInicial(cantidad);
        lote.setCantidadActual(cantidad);
        lote.setFechaIngreso(LocalDate.now());
        lote.setFechaVencimiento(fechaVencimiento);
        lote.setPrecioCompra(precioCompra);
        lote.setEstadoLote(estadoInicial);


        Lote loteGuardado = loteRepository.save(lote);

        // Actualizar stock del producto
        actualizarStockProducto(idProducto);

        return loteGuardado;
    }

    //Con este metodo se exporta el código del lote
    private String generarCodigoLote(Producto producto) {
        // Obtener categoría (2 dígitos)
        int idCategoria = producto.getCategoria().getIdCategoria();
        String codigoCategoria = String.format("%02d", idCategoria);

        // Obtener proveedor desde marca (2 dígitos)
        int idProveedor = producto.getMarca().getProveedor().getIdProveedor();
        String codigoProveedor = String.format("%02d", idProveedor);

        // Fecha actual (DDMMYYYY)
        LocalDate fechaActual = LocalDate.now();
        String fecha = fechaActual.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        // Correlativo del día (3 dígitos)
        Integer cantidadLotesHoy = loteRepository.contarLotesPorFecha(fechaActual);
        int correlativo = (cantidadLotesHoy != null ? cantidadLotesHoy : 0) + 1;
        String codigoCorrelativo = String.format("%03d", correlativo);

        // Construir código final
        return String.format("LT-%s-%s-%s-%s",
                codigoCategoria, codigoProveedor, fecha, codigoCorrelativo);
    }
    // Metodo para calcular estado del lote
    private EstadoLote calcularEstadoLote(int cantidadActual, int cantidadInicial,
                                          LocalDate fechaVencimiento) {
        LocalDate hoy = LocalDate.now();

        // 1. VENCIDO (máxima prioridad)
        if (fechaVencimiento.isBefore(hoy)) {
            return EstadoLote.VENCIDO;
        }

        // 2. AGOTADO
        if (cantidadActual == 0) {
            return EstadoLote.AGOTADO;
        }

        // 3. PROXIMO_A_VENCER (menos de 30 días)
        long diasParaVencer = ChronoUnit.DAYS.between(hoy, fechaVencimiento);
        if (diasParaVencer < 30) {
            return EstadoLote.PROXIMO_A_VENCER;
        }

        // 4. PROXIMO_A_TERMINAR (menos del 15% del stock inicial)
        double porcentajeRestante = (double) cantidadActual / cantidadInicial;
        if (porcentajeRestante < 0.15) {
            return EstadoLote.PROXIMO_A_TERMINAR;
        }

        // 5. DISPONIBLE (estado por defecto)
        return EstadoLote.DISPONIBLE;
    }
    //Metodo público para actualizar estado de lotes existentes
    @Transactional
    public void actualizarEstadoLote(Lote lote) {
        EstadoLote nuevoEstado = calcularEstadoLote(
                lote.getCantidadActual(),
                lote.getCantidadInicial(),
                lote.getFechaVencimiento()
        );
        lote.setEstadoLote(nuevoEstado);
        loteRepository.save(lote);
    }



    // Listar lotes de un producto
    @Transactional(readOnly = true)
    public List<Lote> listarLotesPorProducto(Integer idProducto) {
        return loteRepository.findByProducto_IdProductoOrderByFechaIngresoDesc(idProducto);
    }

    // Actualizar stock total del producto
    private void actualizarStockProducto(Integer idProducto) {
        int stockTotal = loteRepository.calcularStockTotal(idProducto);
        Producto producto = productoRepository.findById(idProducto).orElseThrow();
        producto.setStock(stockTotal);
        productoRepository.save(producto);
    }
}