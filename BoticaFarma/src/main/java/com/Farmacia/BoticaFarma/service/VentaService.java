package com.Farmacia.BoticaFarma.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Farmacia.BoticaFarma.model.*;
import com.Farmacia.BoticaFarma.repository.*;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetalleVentaLoteRepository detalleVentaLoteRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AlmacenRepository almacenRepository; // <--- INYECTADO

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Autowired
    private LoteService loteService;

    // DTOs internos se mantienen igual...
    public static class DetalleVentaDTO {
        private Integer idProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        public Integer getIdProducto() { return idProducto; }
        public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    }

    public static class VentaDTO {
        private Integer idUsuario;
        private Metodopago metodoPago;
        private List<DetalleVentaDTO> detalles;
        private String tipoComprobante;
        private ClienteDTO cliente;
        private String tipoVenta;
        private DeliveryDTO delivery;

        public Integer getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
        public Metodopago getMetodoPago() { return metodoPago; }
        public void setMetodoPago(Metodopago metodoPago) { this.metodoPago = metodoPago; }
        public List<DetalleVentaDTO> getDetalles() { return detalles; }
        public void setDetalles(List<DetalleVentaDTO> detalles) { this.detalles = detalles; }
        public String getTipoComprobante() { return tipoComprobante; }
        public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }
        public ClienteDTO getCliente() { return cliente; }
        public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }
        public String getTipoVenta() { return tipoVenta; }
        public void setTipoVenta(String tipoVenta) { this.tipoVenta = tipoVenta; }
        public DeliveryDTO getDelivery() { return delivery; }
        public void setDelivery(DeliveryDTO delivery) { this.delivery = delivery; }
    }

    public static class DeliveryDTO {
        private Integer idRepartidor;
        private BigDecimal distanciaKm;
        private BigDecimal costoEnvio;
        private String linkCliente;

        public Integer getIdRepartidor() { return idRepartidor; }
        public void setIdRepartidor(Integer idRepartidor) { this.idRepartidor = idRepartidor; }
        public BigDecimal getDistanciaKm() { return distanciaKm; }
        public void setDistanciaKm(BigDecimal distanciaKm) { this.distanciaKm = distanciaKm; }
        public BigDecimal getCostoEnvio() { return costoEnvio; }
        public void setCostoEnvio(BigDecimal costoEnvio) { this.costoEnvio = costoEnvio; }
        public String getLinkCliente() { return linkCliente; }
        public void setLinkCliente(String linkCliente) { this.linkCliente = linkCliente; }
    }

    public static class ClienteDTO {
        private Integer idCliente;
        private String nombre;
        private String apellidoPat;
        private String apellidoMat;
        private String dni;
        private String ruc;

        public Integer getIdCliente() { return idCliente; }
        public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellidoPat() { return apellidoPat; }
        public void setApellidoPat(String apellidoPat) { this.apellidoPat = apellidoPat; }
        public String getApellidoMat() { return apellidoMat; }
        public void setApellidoMat(String apellidoMat) { this.apellidoMat = apellidoMat; }
        public String getDni() { return dni; }
        public void setDni(String dni) { this.dni = dni; }
        public String getRuc() { return ruc; }
        public void setRuc(String ruc) { this.ruc = ruc; }
    }

    // Método procesarVenta actualizado con idAlmacen
    @Transactional
    public Venta procesarVenta(Integer idAlmacen, VentaDTO ventaDTO) {
        if (ventaDTO.getDetalles() == null || ventaDTO.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }

        Usuario usuario = usuarioRepository.findById(ventaDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Sucursal/Almacén no encontrado con ID: " + idAlmacen));

        Cliente cliente = null;
        if (ventaDTO.getCliente() != null && ventaDTO.getCliente().getIdCliente() != null) {
            cliente = clienteRepository.findById(ventaDTO.getCliente().getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else if (ventaDTO.getCliente() != null &&
                ventaDTO.getCliente().getNombre() != null &&
                !ventaDTO.getCliente().getNombre().trim().isEmpty()) {
            ClienteDTO clienteDTO = ventaDTO.getCliente();
            cliente = new Cliente();
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setApellidoPat(clienteDTO.getApellidoPat());
            cliente.setApellidoMat(clienteDTO.getApellidoMat());
            cliente.setDNI(Integer.parseInt(clienteDTO.getDni()));
            if (clienteDTO.getRuc() != null && !clienteDTO.getRuc().trim().isEmpty()) {
                cliente.setRUC((long) Integer.parseInt(clienteDTO.getRuc()));
            }
            cliente = clienteRepository.save(cliente);
        }
        if (cliente == null) {
            cliente = clienteRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Cliente común no encontrado (ID=1)"));
        }

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setAlmacen(almacen); // <--- REGISTRADO EN EL LOCAL ACTIVO
        venta.setFecha(LocalDateTime.now());
        venta.setMetodopago(ventaDTO.getMetodoPago());
        venta.setTotal(BigDecimal.ZERO);

        Venta ventaGuardada = ventaRepository.save(venta);

        BigDecimal totalVenta = BigDecimal.ZERO;

        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getIdProducto()));

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
            }

            BigDecimal subtotal = detalleDTO.getPrecioUnitario()
                    .multiply(new BigDecimal(detalleDTO.getCantidad()));

            Detalle_venta detalleVenta = new Detalle_venta();
            detalleVenta.setVenta(ventaGuardada);
            detalleVenta.setProducto(producto);
            detalleVenta.setCliente(cliente);
            detalleVenta.setCantidad(detalleDTO.getCantidad());
            detalleVenta.setPrecio_unitario(detalleDTO.getPrecioUnitario());
            detalleVenta.setSubtotal(subtotal);

            Detalle_venta detalleGuardado = detalleVentaRepository.save(detalleVenta);

            descontarStockConFIFO(detalleGuardado, producto, detalleDTO.getCantidad());

            totalVenta = totalVenta.add(subtotal);
        }

        ventaGuardada.setTotal(totalVenta);
        Venta ventaFinal = ventaRepository.save(ventaGuardada);

        try {
            String rutaPDF = pdfGeneratorService.generarComprobante(ventaGuardada, cliente, ventaDTO.getTipoComprobante());
            System.out.println("PDF generado en: " + rutaPDF);
        } catch (Exception e) {
            System.err.println("No se pudo generar el PDF: " + e.getMessage());
        }
        return ventaFinal;
    }

    private void descontarStockConFIFO(Detalle_venta detalleVenta, Producto producto, int cantidadAVender) {
        List<Lote> lotes = loteRepository.findByProducto_IdProductoOrderByFechaIngresoAsc(producto.getIdProducto());
        int cantidadRestante = cantidadAVender;

        for (Lote lote : lotes) {
            if (cantidadRestante <= 0) break;

            if (lote.getEstadoLote() != EstadoLote.DISPONIBLE &&
                    lote.getEstadoLote() != EstadoLote.PROXIMO_A_VENCER &&
                    lote.getEstadoLote() != EstadoLote.PROXIMO_A_TERMINAR) {
                continue;
            }

            if (lote.getCantidadActual() > 0) {
                int cantidadADescontar = Math.min(cantidadRestante, lote.getCantidadActual());
                lote.setCantidadActual(lote.getCantidadActual() - cantidadADescontar);
                loteRepository.save(lote);

                DetalleVentaLote detalleVentaLote = new DetalleVentaLote();
                detalleVentaLote.setDetalleVenta(detalleVenta);
                detalleVentaLote.setLote(lote);
                detalleVentaLote.setCantidadDescontada(cantidadADescontar);
                detalleVentaLoteRepository.save(detalleVentaLote);

                loteService.actualizarEstadoLote(lote);
                cantidadRestante -= cantidadADescontar;
            }
        }

        if (cantidadRestante > 0) {
            throw new RuntimeException("No hay suficiente stock en lotes para: " + producto.getNombreProducto());
        }

        actualizarStockProducto(producto.getIdProducto());
    }

    private void actualizarStockProducto(Integer idProducto) {
        int stockTotal = loteRepository.calcularStockTotal(idProducto);
        Producto producto = productoRepository.findById(idProducto).orElseThrow();
        producto.setStock(stockTotal);
        productoRepository.save(producto);
    }

    // Listar ventas por sucursal
    @Transactional(readOnly = true)
    public List<Venta> listarVentas(Integer idAlmacen) {
        return ventaRepository.findByAlmacen_IdAlmacen(idAlmacen);
    }

    @Transactional(readOnly = true)
    public Venta obtenerVentaPorId(Integer idVenta) {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Detalle_venta> obtenerDetallesVenta(Integer idVenta) {
        return detalleVentaRepository.findByVenta_IdVenta(idVenta);
    }
}