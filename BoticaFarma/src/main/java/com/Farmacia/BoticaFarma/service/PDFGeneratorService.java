package com.Farmacia.BoticaFarma.service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Farmacia.BoticaFarma.model.Cliente;
import com.Farmacia.BoticaFarma.model.Detalle_venta;
import com.Farmacia.BoticaFarma.model.Venta;
import com.Farmacia.BoticaFarma.repository.DetalleVentaRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PDFGeneratorService {

    private final DetalleVentaRepository detalleVentaRepository;

    
    @Value("${PDF_DIRECTORY}")
    private String pdfDirectory;

    public PDFGeneratorService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public String generarComprobante(Venta venta, Cliente cliente, String tipoComprobante) {
        String rutaCompleta = "";

        try {
            // Obtener los detalles desde el repository
            List<Detalle_venta> detalles = detalleVentaRepository.findByVenta_IdVenta(venta.getIdVenta());

            // Crear directorios
            String directorioBase = pdfDirectory + "/" + (tipoComprobante.equals("FACTURA") ? "facturas" : "boletas");
            File directorio = new File(directorioBase);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Generar nombre archivo
            String nombreArchivo = generarNombreArchivo(venta, tipoComprobante);
            rutaCompleta = directorioBase + "/" + nombreArchivo;

            // Crear documento PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
            document.open();

            // Agregar contenido al PDF
            agregarContenidoPDF(document, venta, detalles, cliente, tipoComprobante);

            document.close();

            System.out.println("PDF generado en: " + rutaCompleta);

        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }

        return rutaCompleta;
    }

    private void agregarContenidoPDF(Document document, Venta venta, List<Detalle_venta> detalles, Cliente cliente, String tipoComprobante) throws Exception {
        // Encabezado
        document.add(new Paragraph("BOTICAFARMA", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD)));
        document.add(new Paragraph(tipoComprobante + " ELECTRÓNICA", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD)));
        document.add(new Paragraph("RUC: 20123456789"));
        document.add(new Paragraph("Av. Principal 123 - Lima, Perú"));
        document.add(new Paragraph(" "));

        // Datos del comprobante
        document.add(new Paragraph("N° Comprobante: " + venta.getIdVenta()));
        document.add(new Paragraph("Fecha Emisión: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        document.add(new Paragraph("Método Pago: " + venta.getMetodopago()));
        document.add(new Paragraph(" "));

        // Datos del cliente
        if (cliente != null) {
            document.add(new Paragraph("Cliente: " + cliente.getNombre() + " " + cliente.getApellido_pat() + " " + cliente.getApellido_mat()));
            document.add(new Paragraph("DNI: " + cliente.getDNI()));
            if (tipoComprobante.equals("FACTURA")) {
                document.add(new Paragraph("RUC: " + cliente.getRUC()));
            }
        } else {
            document.add(new Paragraph("Cliente: CLIENTE COMÚN"));
            document.add(new Paragraph("DNI: 00000000"));
        }
        document.add(new Paragraph(" "));

        // Tabla de productos
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        // Encabezados de tabla
        table.addCell(crearCelda("Producto", true));
        table.addCell(crearCelda("Cantidad", true));
        table.addCell(crearCelda("Precio Unit.", true));
        table.addCell(crearCelda("Subtotal", true));

        // Productos
        for (Detalle_venta detalle : detalles) {
            table.addCell(crearCelda(detalle.getProducto().getNombreProducto(), false));
            table.addCell(crearCelda(String.valueOf(detalle.getCantidad()), false));
            table.addCell(crearCelda("S/ " + detalle.getPrecio_unitario(), false));
            table.addCell(crearCelda("S/ " + detalle.getSubtotal(), false));
        }

        document.add(table);
        document.add(new Paragraph(" "));

        // Totales
        double subtotal = venta.getTotal().doubleValue() / 1.18;
        double igv = venta.getTotal().doubleValue() * 0.18;

        document.add(new Paragraph("Subtotal: S/ " + String.format("%.2f", subtotal)));
        document.add(new Paragraph("IGV (18%): S/ " + String.format("%.2f", igv)));
        document.add(new Paragraph("TOTAL: S/ " + venta.getTotal(),
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD)));
        document.add(new Paragraph(" "));

        // Pie de página
        document.add(new Paragraph("¡Gracias por su compra!"));
        document.add(new Paragraph("Este es un comprobante electrónico generado automáticamente"));
    }

    private PdfPCell crearCelda(String texto, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Paragraph(texto));
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
        }
        return cell;
    }

    private String generarNombreArchivo(Venta venta, String tipoComprobante) {
        String prefijo = tipoComprobante.equals("FACTURA") ? "factura" : "boleta";
        return String.format("%s_%d.pdf", prefijo, venta.getIdVenta());
    }
}