package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.dto.ReporteVentaDTO;
import com.Farmacia.BoticaFarma.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/boticafarma/reportes") // Ruta específica para reportes
@CrossOrigin(origins = "http://localhost:5173")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // ==========================================
    //      1. ENDPOINTS PARA GRÁFICOS (JSON)
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

    // ==========================================
    //      2. ENDPOINTS PARA PDF (Descargas)
    // ==========================================

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
}