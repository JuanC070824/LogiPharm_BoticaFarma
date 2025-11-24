package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.ReporteVentaDTO;
import com.Farmacia.BoticaFarma.repository.VentaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.Color;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private VentaRepository ventaRepository;

    // ==========================================
    // 1. GENERACIÓN DE PDFS (Lógica Visual)
    // ==========================================

    // PDF MENSUAL
    public ByteArrayInputStream generarReporteVentasPdf() {
        // Llamamos al método genérico pasándole los datos MENSUALES
        return generarPdfGenerico(
                ventaRepository.obtenerVentasMensuales(),
                "Reporte de Ventas Mensuales",
                "Mes"
        );
    }

    // PDF DIARIO (¡Esto es lo que faltaba!)
    public ByteArrayInputStream generarReporteVentasDiariasPdf() {
        // Llamamos al método genérico pasándole los datos DIARIOS
        return generarPdfGenerico(
                ventaRepository.obtenerVentasDiarias(),
                "Reporte de Ventas Diarias",
                "Fecha"
        );
    }

    // ==========================================
    // 2. DATOS JSON (Para el Frontend)
    // ==========================================

    public List<ReporteVentaDTO> obtenerDatosVentasMensuales() {
        return mapearA_DTO(ventaRepository.obtenerVentasMensuales());
    }

    public List<ReporteVentaDTO> obtenerDatosVentasDiarias() {
        return mapearA_DTO(ventaRepository.obtenerVentasDiarias());
    }

    // ==========================================
    // 3. MÉTODOS PRIVADOS (Auxiliares)
    // ==========================================

    // Lógica común para crear el PDF (Evita repetir código)
    private ByteArrayInputStream generarPdfGenerico(List<Object[]> resultadosCrudos, String tituloReporte, String nombreColumnaFecha) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Convertimos los datos crudos a una lista limpia
            List<ReporteVentaDTO> datos = mapearA_DTO(resultadosCrudos);

            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph titulo = new Paragraph(tituloReporte, fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            // Tabla
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell(getHeaderCell(nombreColumnaFecha)); // "Mes" o "Fecha"
            table.addCell(getHeaderCell("Total Vendido"));

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (ReporteVentaDTO dato : datos) {
                table.addCell(dato.getEtiqueta());
                table.addCell("S/ " + String.format("%.2f", dato.getTotal()));

                // Llenamos datos para el gráfico interno del PDF
                dataset.addValue(dato.getTotal(), "Ventas", dato.getEtiqueta());
            }
            document.add(table);
            document.add(Chunk.NEWLINE);

            // Gráfico dentro del PDF
            JFreeChart chart = ChartFactory.createBarChart(
                    "Evolución de Ventas", "Periodo", "Monto (S/)",
                    dataset, PlotOrientation.VERTICAL, false, true, false);
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);
            plot.setRangeGridlinePaint(new Color(220, 220, 220));


            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setBarPainter(new StandardBarPainter());
            renderer.setShadowVisible(false);


            renderer.setSeriesPaint(0, new Color(59, 130, 246));


            chart.getTitle().setPaint(new Color(30, 41, 59));
            plot.getDomainAxis().setTickLabelPaint(new Color(100, 116, 139));
            plot.getRangeAxis().setTickLabelPaint(new Color(100, 116, 139));
            int width = 500;
            int height = 300;
            java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(width, height);
            Image image = Image.getInstance(writer, bufferedImage, 1.0f);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }



    // Convierte la lista de Object[] (BD) a ReporteVentaDTO
    private List<ReporteVentaDTO> mapearA_DTO(List<Object[]> resultadosCrudos) {
        List<ReporteVentaDTO> datos = new ArrayList<>();
        for (Object[] fila : resultadosCrudos) {
            String etiqueta = (fila[0] != null) ? fila[0].toString() : "Sin fecha";
            Double total = (fila[1] != null) ? ((Number) fila[1]).doubleValue() : 0.0;
            datos.add(new ReporteVentaDTO(etiqueta, total));
        }
        return datos;
    }

    private PdfPCell getHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }
}