package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.model.Detalle_venta;
import com.Farmacia.BoticaFarma.model.Venta;
import com.Farmacia.BoticaFarma.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/ventas")
@CrossOrigin(origins = "http://localhost:5173")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Procesar una venta
    @PostMapping
    public ResponseEntity<Map<String, Object>> procesarVenta(@RequestBody VentaService.VentaDTO ventaDTO) {
        try {
            Venta venta = ventaService.procesarVenta(ventaDTO);

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

    // Listar todas las ventas
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarVentas() {
        try {
            List<Venta> ventas = ventaService.listarVentas();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ventas", ventas);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al listar ventas: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Obtener detalle de una venta específica
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
            return ResponseEntity.badRequest().body(response);
        }
    }
}