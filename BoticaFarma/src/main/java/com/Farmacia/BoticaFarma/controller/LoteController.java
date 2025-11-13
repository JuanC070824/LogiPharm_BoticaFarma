package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.model.Lote;
import com.Farmacia.BoticaFarma.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boticafarma/lotes")
@CrossOrigin(origins = "http://localhost:5173")
public class LoteController {

    @Autowired
    private LoteService loteService;


    //Con esto me aseguro que solo el ADMIN ponga los lotes
    @PreAuthorize("hasRole('ADMIN')")
    // Crear lote
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearLote(@RequestBody Map<String, Object> request) {
        try {
            Integer idProducto = (Integer) request.get("idProducto");
            Integer idUsuario = (Integer) request.get("idUsuario");
            Integer cantidad = (Integer) request.get("cantidad");
            String fechaVencimientoStr = (String) request.get("fechaVencimiento");
            //String codigoLote = (String) request.get("codigoLote");
            //Obtengo el precio de compra del lote
            BigDecimal precioCompra;
            Object precioCompraObj = request.get("precioCompra");
            if (precioCompraObj instanceof Integer) {
                precioCompra = new BigDecimal((Integer) precioCompraObj);
            } else if (precioCompraObj instanceof Double) {
                precioCompra = BigDecimal.valueOf((Double) precioCompraObj);
            } else if (precioCompraObj instanceof String) {
                precioCompra = new BigDecimal((String) precioCompraObj);
            } else {
                throw new RuntimeException("Precio de compra inválido");
            }


            LocalDate fechaVencimiento = LocalDate.parse(fechaVencimientoStr);

            Lote lote = loteService.crearLote(idProducto, idUsuario, cantidad,
                    fechaVencimiento, precioCompra);


            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lote creado correctamente");
            response.put("lote", lote);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear lote: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    // Listar lotes por producto, para ambos roles
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<Map<String, Object>> listarLotesPorProducto(@PathVariable Integer idProducto) {
        try {
            List<Lote> lotes = loteService.listarLotesPorProducto(idProducto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("lotes", lotes);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al listar lotes: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}