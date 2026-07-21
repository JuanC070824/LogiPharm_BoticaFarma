package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.ReporteVentaDTO;
import com.Farmacia.BoticaFarma.model.Detalle_venta;
import com.Farmacia.BoticaFarma.model.Venta;
import com.Farmacia.BoticaFarma.service.ReporteService;
import com.Farmacia.BoticaFarma.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/ventas")
@CrossOrigin(origins = "http://localhost:5173")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ReporteService reporteService;

    // ==========================================
    //      ZONA DE REPORTES (MULTI-TENANT)
    // ==========================================

    @GetMapping("/mensuales/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReporteVentaDTO>> obtenerDatosGraficoMensual() {
        return ResponseEntity.ok(reporteService.obtenerDatosVentasMensuales());
    }

    @GetMapping("/diarias/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReporteVentaDTO>> obtenerDatosGraficoDiario() {
        return ResponseEntity.ok(reporteService.obtenerDatosVentasDiarias());
    }

    @GetMapping("/mensuales/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> descargarPdfMensual() {
        ByteArrayInputStream bis = reporteService.generarReporteVentasPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_ventas_mensual.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/diarias/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> descargarPdfDiario() {
        ByteArrayInputStream bis = reporteService.generarReporteVentasDiariasPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_ventas_diario.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // ==========================================
    //      ZONA DE GESTIÓN DE VENTAS
    // ==========================================

    // Procesar Venta (Asociada al local activo)
    @PostMapping
    public ResponseEntity<Map<String, Object>> procesarVenta(
            @RequestAttribute("idAlmacen") Integer idAlmacen,
            @RequestBody VentaService.VentaDTO ventaDTO
    ) {
        try {
            Venta venta = ventaService.procesarVenta(idAlmacen, ventaDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Venta procesada correctamente");
            response.put("venta", venta);
            response.put("idVenta", venta.getIdVenta());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al procesar venta: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Listar Ventas (Filtradas por la sucursal activa)
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarVentas(
            @RequestAttribute("idAlmacen") Integer idAlmacen
    ) {
        try {
            List<Venta> ventas = ventaService.listarVentas(idAlmacen);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ventas", ventas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al listar ventas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Obtener Venta por ID y sus detalles
    @GetMapping("/{idVenta}")
    public ResponseEntity<Map<String, Object>> obtenerVenta(@PathVariable Integer idVenta) {
        try {
            Venta venta = ventaService.obtenerVentaPorId(idVenta);
            List<Detalle_venta> detalles = ventaService.obtenerDetallesVenta(idVenta);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("venta", venta);
            response.put("detalles", detalles);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}