package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.model.*;
import com.Farmacia.BoticaFarma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private PDFGeneratorService pdfGeneratorService;

    @Autowired
    private LoteService loteService; // Para actualizar estados de lotes

    // DTO interno para recibir datos del frontend
    public static class DetalleVentaDTO {
        private Integer idProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        // Getters y setters
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
        private String tipoComprobante; // "BOLETA" o "FACTURA"
        private ClienteDTO cliente;
        // Getters y setters
        public Integer getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
        public Metodopago getMetodoPago() { return metodoPago; }
        public void setMetodoPago(Metodopago metodoPago) { this.metodoPago = metodoPago; }
        public List<DetalleVentaDTO> getDetalles() { return detalles; }
        public void setDetalles(List<DetalleVentaDTO> detalles) { this.detalles = detalles; }
        public String getTipoComprobante() { return tipoComprobante; }
        public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante;
        }
        public ClienteDTO getCliente() { return cliente; }
        public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }
    }
    public static class ClienteDTO {
        private Integer idCliente;
        private String nombre;
        private String apellidoPat;
        private String apellidoMat;
        private String dni;
        private String ruc;


        public Integer getIdCliente() {
            return idCliente;
        }
        public void setIdCliente(Integer idCliente) {
            this.idCliente = idCliente;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getApellidoPat() {
            return apellidoPat;
        }
        public void setApellidoPat(String apellidoPat) {
            this.apellidoPat = apellidoPat;
        }
        public String getApellidoMat() {
            return apellidoMat;
        }
        public void setApellidoMat(String apellidoMat) {
            this.apellidoMat = apellidoMat;
        }
        public String getDni() {
            return dni;
        }
        public void setDni(String dni) {
            this.dni = dni;
        }
        public String getRuc() {
            return ruc;
        }
        public void setRuc(String ruc) {
            this.ruc = ruc;
        }
    }

    // Metodo principal para procesar una venta
    @Transactional
    public Venta procesarVenta(VentaDTO ventaDTO) {
        // 1. Validar que haya detalles
        if (ventaDTO.getDetalles() == null || ventaDTO.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }

        // 2. Obtener el usuario
        Usuario usuario = usuarioRepository.findById(ventaDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //2.5 aquí manejamos al cliente
        Cliente cliente = null;
        if (ventaDTO.getCliente() != null && ventaDTO.getCliente().getIdCliente() != null) {
            // Cliente existente (registrado)
            cliente = clienteRepository.findById(ventaDTO.getCliente().getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else if (ventaDTO.getCliente() != null &&
                ventaDTO.getCliente().getNombre() != null &&
                !ventaDTO.getCliente().getNombre().trim().isEmpty()) {
            // Nuevo cliente registrado
            ClienteDTO clienteDTO = ventaDTO.getCliente();
            cliente = new Cliente();
            cliente.setNombre(clienteDTO.getNombre());

            cliente.setApellido_pat(clienteDTO.getApellidoPat());
            cliente.setApellido_mat(clienteDTO.getApellidoMat());
            cliente.setDNI(Integer.parseInt(clienteDTO.getDni()));
            if (clienteDTO.getRuc() != null && !clienteDTO.getRuc().trim().isEmpty()) {
                cliente.setRUC(Integer.parseInt(clienteDTO.getRuc()));
            }
            cliente = clienteRepository.save(cliente);
        }
        if (cliente == null) {
            cliente = clienteRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Cliente común no encontrado (ID=1)"));
        }


        // 3. Crear la venta (sin total aún)
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFecha(LocalDateTime.now());
        venta.setMetodopago(ventaDTO.getMetodoPago());
        venta.setTotal(BigDecimal.ZERO); // Lo calcularemos después

        Venta ventaGuardada = ventaRepository.save(venta);

        // 4. Procesar cada detalle
        BigDecimal totalVenta = BigDecimal.ZERO;

        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getIdProducto()));

            // Verificar stock disponible
            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
            }

            // Calcular subtotal
            BigDecimal subtotal = detalleDTO.getPrecioUnitario()
                    .multiply(new BigDecimal(detalleDTO.getCantidad()));

            // Crear detalle de venta
            Detalle_venta detalleVenta = new Detalle_venta();
            detalleVenta.setVenta(ventaGuardada);
            detalleVenta.setProducto(producto);

            detalleVenta.setCliente(cliente);// Opcional, si no manejas clientes aún
            detalleVenta.setCantidad(detalleDTO.getCantidad());
            detalleVenta.setPrecio_unitario(detalleDTO.getPrecioUnitario());
            detalleVenta.setSubtotal(subtotal);

            Detalle_venta detalleGuardado = detalleVentaRepository.save(detalleVenta);

            // FIFO: Descontar del stock usando los lotes más antiguos
            descontarStockConFIFO(detalleGuardado, producto, detalleDTO.getCantidad());

            // Acumular total
            totalVenta = totalVenta.add(subtotal);
        }

        // 5. Actualizar el total de la venta
        ventaGuardada.setTotal(totalVenta);
        Venta ventaFinal = ventaRepository.save(ventaGuardada);

        try {
            String rutaPDF = pdfGeneratorService.generarComprobante(ventaGuardada, cliente, ventaDTO.getTipoComprobante());
            System.out.println("PDF generado en: " + rutaPDF);
        } catch (Exception e) {
            System.err.println(" No se pudo generar el PDF: " + e.getMessage());
            // No lanzamos excepción para no revertir la venta
        }
        return ventaFinal;
    }

    // Metodo privado para aplicar FIFO
    private void descontarStockConFIFO(Detalle_venta detalleVenta, Producto producto, int cantidadAVender) {
        // Obtener lotes ordenados por fecha de ingreso (FIFO)
        List<Lote> lotes = loteRepository.findByProducto_IdProductoOrderByFechaIngresoAsc(producto.getIdProducto());

        int cantidadRestante = cantidadAVender;

        for (Lote lote : lotes) {
            if (cantidadRestante <= 0) {
                break; // Ya se vendió toda la cantidad
            }
            if (lote.getEstadoLote() != EstadoLote.DISPONIBLE &&
                    lote.getEstadoLote() != EstadoLote.PROXIMO_A_VENCER &&
                    lote.getEstadoLote() != EstadoLote.PROXIMO_A_TERMINAR) {
                continue; // Saltar lotes VENCIDOS o AGOTADOS
            }

            if (lote.getCantidadActual() > 0) {
                // Calcular cuánto descontar de este lote
                int cantidadADescontar = Math.min(cantidadRestante, lote.getCantidadActual());

                // Actualizar cantidad del lote
                lote.setCantidadActual(lote.getCantidadActual() - cantidadADescontar);
                loteRepository.save(lote);

                // Registrar en DetalleVentaLote
                DetalleVentaLote detalleVentaLote = new DetalleVentaLote();
                detalleVentaLote.setDetalleVenta(detalleVenta);
                detalleVentaLote.setLote(lote);
                detalleVentaLote.setCantidadDescontada(cantidadADescontar);
                detalleVentaLoteRepository.save(detalleVentaLote);

                // Actualizar estado del lote
                loteService.actualizarEstadoLote(lote);

                // Reducir la cantidad restante
                cantidadRestante -= cantidadADescontar;
            }
        }

        // Si aún queda cantidad por vender, hay un problema
        if (cantidadRestante > 0) {
            throw new RuntimeException("No hay suficiente stock en lotes para: " + producto.getNombreProducto());
        }

        // Actualizar stock total del producto
        actualizarStockProducto(producto.getIdProducto());
    }

    // Actualizar stock total del producto
    private void actualizarStockProducto(Integer idProducto) {
        int stockTotal = loteRepository.calcularStockTotal(idProducto);
        Producto producto = productoRepository.findById(idProducto).orElseThrow();
        producto.setStock(stockTotal);
        productoRepository.save(producto);
    }

    // Listar todas las ventas
    @Transactional(readOnly = true)
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    // Obtener una venta por ID
    @Transactional(readOnly = true)
    public Venta obtenerVentaPorId(Integer idVenta) {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    // Obtener detalles de una venta
    @Transactional(readOnly = true)
    public List<Detalle_venta> obtenerDetallesVenta(Integer idVenta) {
        return detalleVentaRepository.findByVenta_IdVenta(idVenta);
    }
}