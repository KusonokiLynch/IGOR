package com.example.IGORPROYECTO.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.model.Proyecto;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfService {

    public void exportProjectPdfWithDocs(HttpServletResponse response,
                                         Proyecto proyecto,
                                         List<Documentacion> documentos) throws Exception {

        // Configurar tipo de contenido
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_proyecto.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // --- Título principal ---
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        document.add(new Paragraph("Reporte de Proyecto", titleFont));
        document.add(new Paragraph(" ")); // Espacio

        // --- Información del proyecto ---
        Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Nombre: " + proyecto.getNombre(), bold));
        document.add(new Paragraph("Descripción: " + proyecto.getDescripcion(), normal));
        document.add(new Paragraph("Cliente: " + proyecto.getCliente(), normal));
        document.add(new Paragraph("Responsable: " + proyecto.getResponsable(), normal));
        document.add(new Paragraph("Estado: " + proyecto.getEstado(), normal));
        document.add(new Paragraph("Programa: " + proyecto.getPrograma(), normal));
        document.add(new Paragraph("Fecha Inicio: " + proyecto.getFechaInicio(), normal));
        document.add(new Paragraph("Fecha Final: " + proyecto.getFechaFinal(), normal));
        document.add(new Paragraph("Serial: " + proyecto.getSerial(), normal));
        document.add(new Paragraph(" ")); // Espacio

        // --- Tabla de documentación ---
        Font tableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font tableCell = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPTable table = new PdfPTable(5); // 5 columnas
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Encabezados
        addTableHeader(table, "Nombre", tableHeader);
        addTableHeader(table, "Tipo", tableHeader);
        addTableHeader(table, "Descripción", tableHeader);
        addTableHeader(table, "Estado", tableHeader);
        addTableHeader(table, "Fecha", tableHeader);

        // Filas
        for (Documentacion doc : documentos) {
            addTableCell(table, doc.getNombre(), tableCell);
            addTableCell(table, doc.getTipo(), tableCell);
            addTableCell(table, doc.getDescripcion(), tableCell);
            addTableCell(table, doc.getEstado(), tableCell);
            addTableCell(table, (doc.getFechaCreacion() != null ? doc.getFechaCreacion().toString() : "-"), tableCell);
        }

        document.add(new Paragraph("Documentación Relacionada:", bold));
        document.add(table);

        document.close();
    }

    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell header = new PdfPCell();
        header.setPhrase(new Paragraph(text, font));
        header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Paragraph(text != null ? text : "", font));
        table.addCell(cell);
    }
}
