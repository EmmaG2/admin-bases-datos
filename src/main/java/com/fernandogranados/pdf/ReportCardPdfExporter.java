package com.fernandogranados.pdf;

import com.fernandogranados.model.Alumno;
import com.fernandogranados.model.ReportCard;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class ReportCardPdfExporter {
  public void export(Alumno student, ReportCard report, Path destination) throws IOException {
    Path target = destination.toAbsolutePath();
    Path temporary = Files.createTempFile(target.getParent(), ".report-card-", ".pdf");
    try {
      try (PdfDocument pdf = new PdfDocument(new PdfWriter(temporary.toString()));
           Document document = new Document(pdf)) {
        document.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
        document.setFontSize(11);
        document.add(new Paragraph("Boleta de calificaciones").setFontSize(22));
        document.add(new Paragraph(student.getNombre()).setFontSize(16));
        document.add(new Paragraph("Matrícula: " + student.getMatricula()
            + "\nEspecialidad: " + student.getEspecialidad()));
        document.add(new Paragraph("Materias de Sistemas"));
        if (report.grades().isEmpty()) {
          document.add(new Paragraph("Sin calificaciones registradas para estas materias."));
        } else {
          Table table = new Table(UnitValue.createPercentArray(new float[] {6, 2, 2})).useAllAvailableWidth();
          table.addHeaderCell("Materia");
          table.addHeaderCell("Periodo");
          table.addHeaderCell("Calificación / 100");
          for (ReportCard.Grade grade : report.grades()) {
            table.addCell(grade.subject());
            table.addCell(Short.toString(grade.period()));
            table.addCell(Short.toString(grade.grade()));
          }
          document.add(table);
        }
        document.add(new Paragraph("Promedio general (todas las materias): "
            + (report.average() == null ? "No disponible"
                : String.format(Locale.forLanguageTag("es-MX"), "%.2f / 100", report.average()))));
      }
      Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
    } finally {
      Files.deleteIfExists(temporary);
    }
  }
}
